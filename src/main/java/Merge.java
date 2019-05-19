import com.google.common.base.Objects;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main Class, merges Epost documents to one file.
 */
public class Merge {

    public File[] getSrcFolder() {
        return srcFolder;
    }

    public void setSrcFolder(File[] srcFolder) {
        this.srcFolder = srcFolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Merge)) return false;
        Merge merge = (Merge) o;
        return Objects.equal(getSrcFolder(), merge.getSrcFolder()) &&
                Objects.equal(destFolder, merge.destFolder);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSrcFolder(), destFolder);
    }

    // Source srcFolder
    private File[] srcFolder;
    private Path destFolder;

    /**
     * Default constructor.
     *
     * @param folder File array, which is the source srcFolder.
     */
    public Merge(File[] folder) {
        this.srcFolder = folder;
        destFolder = Paths.get(Arrays.toString(srcFolder));
    }

    public Merge(File[] folder, Path destFolder) {
        this.srcFolder = folder;
        this.destFolder = destFolder;
    }

    /**
     * Abstract List constructor
     *
     * @param folder takes a List-Object with File generic as input, and converts it assigns
     *               it to the srcFolder class variable.
     */
    public Merge(List<File> folder) {
        this.srcFolder = new File[folder.size()];
        for (int i = 0; i < folder.size(); i++) {
            this.srcFolder[i] = folder.get(i);
        }
    }

    public Merge(List<File> folder, Path destFolder) {
        this.srcFolder = new File[folder.size()];
        for (int i = 0; i < folder.size(); i++) {
            this.srcFolder[i] = folder.get(i);
        }
        this.destFolder = destFolder;
    }

    /**
     * Filters for the XMLs from the provided srcFolder
     *
     * @return ArrayList containing the xml files.
     */
    private ArrayList<File> filterForXml() {
        return filterForXml(this.srcFolder);
    }

    /**
     * Filters for the XMLs from the provided srcFolder
     *
     * @param folder source srcFolder
     * @return ArrayList containing the xml files.
     */
    ArrayList<File> filterForXml(File[] folder) {
        ArrayList<File> filteredList = new ArrayList<>();
        for (File f : folder) if (f.getName().endsWith(".xml")) filteredList.add(f);

        return filteredList;
    }

    /**
     * Filters the provided srcFolder for a given, non-regular, expression in file names.
     *
     * @param expression expression to find in the file names.
     */
    private ArrayList<File> filterForExpression(String expression) {
        return filterForExpression(this.srcFolder, expression);
    }

    /**
     * Filters the provided srcFolder for a given, non-regular, expression in file names.
     *
     * @param folder     source srcFolder
     * @param expression expression to find in the file names.
     */
    private ArrayList<File> filterForExpression(File[] folder, String expression) {
        ArrayList<File> returnList = new ArrayList<>();
        for (File f : folder) if (f.getName().contains(expression)) returnList.add(f);

        return returnList;
    }

    /**
     * Deserialize a given XML file to the Group object
     *
     * @param file xml source file
     * @return mapped group object.
     * @throws JDOMException thrown if there is a malformed/invalid XML provided
     * @throws IOException   java standard file exceptions
     */
    private Group xmlData(File file) throws JDOMException, IOException {
        return new Group(XmlDeserializer.getKundennummer(file), XmlDeserializer.getSendungsnummer(file),
                XmlDeserializer.getDatum(file));
    }

    /**
     * Merges all PDFs for the given expression.
     *
     * @param expression expression to merge for.
     */
    public void mergePack(String expression) throws IOException {
        this.mergePack(this.srcFolder, expression, this.destFolder);
    }

    /**
     * Merges all PDFs for the given expression.
     *
     * @param destPath   destination srcFolder
     * @param expression expression to merge for.
     */
    public void mergePack(String expression, Path destPath) throws IOException {
        this.mergePack(this.srcFolder, expression, destPath);
    }

    /**
     * Merges all PDFs for the given expression.
     *
     * @param folder     source srcFolder
     * @param destPath   destination srcFolder
     * @param expression expression to merge for.
     */
    public void mergePack(File[] folder, String expression, Path destPath) throws IOException {
        ArrayList<File> input = filterForExpression(folder, expression);
        ArrayList<File> pdFiles = new ArrayList<>();

        // filter out xml
        for (File f : input) {
            if (f.getName().endsWith(".pdf")) pdFiles.add(f);
        }
        PDFMergerUtility PDFMerger = new PDFMergerUtility();
        File destFile = new File(destPath.toFile(), "merged_" + expression + ".pdf");
        if (destFile.exists()) return; // skipping the already existing
        PDFMerger.setDestinationFileName(destFile.toString());
        for (File f : pdFiles) {
            PDFMerger.addSource(f);
        }
        PDFMerger.mergeDocuments();
    }

    /**
     * Merges all PDFs according to their matching XML file
     */
    public void makePacks() throws JDOMException, IOException {
        makePacks(this.destFolder);
    }

    /**
     * Merges all PDFs according to their matching XML file
     *
     * @param destPath path to save to
     */
    public void makePacks(File destPath) throws JDOMException, IOException {
        makePacks(destPath.toPath());
    }

    /**
     * Merges all PDFs according to their matching XML file
     *
     * @param destPath path to save to
     */
    public void makePacks(Path destPath) throws JDOMException, IOException {
        ArrayList<File> xml = filterForXml(this.srcFolder);
        for (File x : xml) {
            Group g = xmlData(x);
            mergePack(this.srcFolder, g.hash, destPath);
        }
    }

    /**
     * Struct to deserialize the XML to.
     */
    class Group {
        String kdnr, sendnr, date, hash;

        Group(String kdnr, String sendnr, String date) {
            this.kdnr = kdnr;
            this.sendnr = sendnr;
            this.date = date;
            this.hash = genId();
        }

        /**
         * generates filename, like the parsed xml without extension
         *
         * @return common filename
         */
        private String genId() {
            String retDate = this.date.replace("-", "");
            return retDate + "_" + this.sendnr;
        }
    }

    /**
     * Main method.
     *
     * @param args takes one or two paths as input; if only one is provided, this is going to be the source
     *             and destination path. If two are provided, this first one is the source path and the
     *             second the destination path.
     */
    public static void main(String[] args) throws JDOMException, IOException {
        File srcPath, destPath;

        if (args.length == 1) {
            srcPath = destPath = new File(args[0]);
        } else if (args.length == 2) {
            srcPath = new File(args[0]);
            destPath = new File(args[1]);
        } else {
            srcPath = destPath = null;
            System.out.println("Invalid Arguments! (PDFMerge, Java)");
            System.exit(2);
        }

        assert (srcPath.exists());
        assert (destPath.exists());

        List<File> filesInFolder = Files.walk(Paths.get(srcPath.getAbsolutePath()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        Merge m = new Merge(filesInFolder, destPath.toPath());
        m.makePacks();
    }

}
