package com.example.android.materialdesigncodelab;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;


public class Unzip_Openbook {

    private Unzip_Openbook[] books;
    private int nBooks;
    private Book book;
    private int currentSpineElementIndex;
    private String currentPage;
    public String[] spineElementPaths;
    // NOTE: currently, counting the number of XHTML pages
    private int pageCount;
    private String decompressedFolder;
    private String pathOPF;
    private static Context context;
    private static String location = Environment.getExternalStorageDirectory()
            + "/epubtempdir/";

    private String fileName;
    FileInputStream fs;
    private String actualCSS = "";

    public Unzip_Openbook(String fileName, String destFolder) throws Exception {

        List<String> spineElements;
        List<SpineReference> spineList;

       /* if (context == null) {
           this.context = context;
        }*/

        this.fileName = fileName;
        this.decompressedFolder = destFolder;

        this.fs = new FileInputStream(fileName);
        this.book = (new EpubReader()).readEpub(fs);


        Spine spine = book.getSpine();
        spineList = spine.getSpineReferences();

        this.currentSpineElementIndex = 0;

        spineElements = new ArrayList<String>();
        pages(spineList, spineElements);
        this.pageCount = spineElements.size();

        this.spineElementPaths = new String[spineElements.size()];

        unzip(fileName, location + decompressedFolder);

        //pathOPF = getPathOPF(location + decompressedFolder);

        for (int i = 0; i < spineElements.size(); ++i) {
            // TODO: is there a robust path joiner in the java libs?
            this.spineElementPaths[i] = "file://" + location
                    + decompressedFolder + "/" + pathOPF + "/"
                    + spineElements.get(i);
        }

        if (spineElements.size() > 0) {
            goToPage(0);
        }
        //createTocFile();*/

    }


    private void pages(List<SpineReference> spineList, List<String> pages) {
        int langIndex;
        String lang;
        String actualPage;


        for (int i = 0; i < spineList.size(); ++i) {
            actualPage = (spineList.get(i)).getResource().getHref();

                    pages.add(actualPage);

        }
    }


    public boolean goToPage(String page) {
        int index = getPageIndex(page);
        boolean res = false;
        if (index >= 0) {

            try {
                goToPage(index);
                res=true;

            } catch (Exception e) {
                res=false;
                e.printStackTrace();
            }
        }
        return res;
    }

    public String getCurrentPageURL() {
        return currentPage;
    }

    public int getPageIndex(String page) {
        int result = -1;

        for (int i = 0; i < this.spineElementPaths.length && result == -1; i++) {
            if (page.equals(this.spineElementPaths[i])) {
                result = i;
            }
        }

        return result;
    }

    public String goToPage(int page) throws Exception {

        String spineElement;
        String extension;
        if (page < 0) {
            page = 0;
        }
        if (page >= this.pageCount) {
            page = this.pageCount - 1;
        }
        this.currentSpineElementIndex = page;

        spineElement = this.spineElementPaths[currentSpineElementIndex];


        this.currentPage = spineElement;


        return spineElement;
    }


    public void destroy() throws IOException {
        closeStream();
        File c = new File(location + decompressedFolder);
        deleteDir(c);
    }

    public void closeStream() throws IOException {
        fs.close();
        book = null;
    }

    // recursively delete a directory
    private void deleteDir(File f) {
        if (f!=null && f.isDirectory())
            for (File child : f.listFiles())
                deleteDir(child);
        if(f!=null) {
            f.delete();
        }
    }

    public String getSpineElementPath(int elementIndex) {
        return spineElementPaths[elementIndex];
    }


    public void unzip(String inputZip, String destinationDirectory)
            throws IOException {
        int BUFFER = 2048;
        List zipFiles = new ArrayList();
        File sourceZipFile = new File(inputZip);
        File unzipDestinationDirectory = new File(destinationDirectory);
        unzipDestinationDirectory.mkdir();

        ZipFile zipFile;
        zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
        Enumeration zipFileEntries = zipFile.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements()) {

            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(unzipDestinationDirectory, currentEntry);

            if (currentEntry.endsWith(".zip")) {
                zipFiles.add(destFile.getAbsolutePath());
            }

            File destinationParent = destFile.getParentFile();
            destinationParent.mkdirs();

            if (!entry.isDirectory()) {
                BufferedInputStream is = new BufferedInputStream(
                        zipFile.getInputStream(entry));
                int currentByte;
                // buffer for writing file
                byte data[] = new byte[BUFFER];

                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos,
                        BUFFER);

                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();

            }

        }
        zipFile.close();

        for (Iterator iter = zipFiles.iterator(); iter.hasNext(); ) {
            String zipName = (String) iter.next();
            unzip(zipName,
                    destinationDirectory
                            + File.separatorChar
                            + zipName.substring(0,
                            zipName.lastIndexOf(R.string.zip)));
        }
    }
}

