import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.afplib.AfpBuilder;
import org.afplib.afplib.AMB;
import org.afplib.afplib.AMI;
import org.afplib.afplib.AfplibFactory;
import org.afplib.afplib.AfplibPackage;
import org.afplib.afplib.BAG;
import org.afplib.afplib.BDT;
import org.afplib.afplib.BOC;
import org.afplib.afplib.BOG;
import org.afplib.afplib.BPG;
import org.afplib.afplib.BPT;
import org.afplib.afplib.CDD;
import org.afplib.afplib.CDDXocBase;
import org.afplib.afplib.CDDYocBase;
import org.afplib.afplib.CGCSGID;
import org.afplib.afplib.DescriptorPosition;
import org.afplib.afplib.EAG;
import org.afplib.afplib.EDT;
import org.afplib.afplib.EOC;
import org.afplib.afplib.EOG;
import org.afplib.afplib.EPG;
import org.afplib.afplib.EPT;
import org.afplib.afplib.FullyQualifiedName;
import org.afplib.afplib.FullyQualifiedNameFQNFormat;
import org.afplib.afplib.FullyQualifiedNameFQNType;
import org.afplib.afplib.MCD;
import org.afplib.afplib.MCDRG;
import org.afplib.afplib.MCF;
import org.afplib.afplib.MCFRG;
import org.afplib.afplib.MappingOption;
import org.afplib.afplib.MappingOptionMapValue;
import org.afplib.afplib.MeasurementUnits;
import org.afplib.afplib.MeasurementUnitsXoaBase;
import org.afplib.afplib.MeasurementUnitsYoaBase;
import org.afplib.afplib.OBD;
import org.afplib.afplib.OBP;
import org.afplib.afplib.OBPRGLength;
import org.afplib.afplib.OBPRefCSys;
import org.afplib.afplib.OBPXoaOrent;
import org.afplib.afplib.OBPXocaOrent;
import org.afplib.afplib.OBPYoaOrent;
import org.afplib.afplib.OBPYocaOrent;
import org.afplib.afplib.OCD;
import org.afplib.afplib.ObjectAreaSize;
import org.afplib.afplib.ObjectAreaSizeSizeType;
import org.afplib.afplib.ObjectClassification;
import org.afplib.afplib.ObjectClassificationObjClass;
import org.afplib.afplib.PGD;
import org.afplib.afplib.PGDXpgBase;
import org.afplib.afplib.PGDYpgBase;
import org.afplib.afplib.PTD;
import org.afplib.afplib.PTDXPBASE;
import org.afplib.afplib.PTDYPBASE;
import org.afplib.afplib.PTX;
import org.afplib.afplib.ResourceLocalIdentifier;
import org.afplib.afplib.ResourceLocalIdentifierResType;
import org.afplib.afplib.SCFL;
import org.afplib.afplib.SEC;
import org.afplib.afplib.SECCOLSPCE;
import org.afplib.afplib.STC;
import org.afplib.afplib.STCFRGCOLOR;
import org.afplib.afplib.STO;
import org.afplib.afplib.STOBORNTION;
import org.afplib.afplib.STOIORNTION;
import org.afplib.afplib.TRN;
import org.afplib.base.SF;
import org.afplib.io.AfpOutputStream;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

 

/**
 * Mix of two demo utilities a) on how to create a new AFP file with a page and some text on it. b) How to split a pdf file in pages.
 *
 */

public class ConvertPDFToAFP {

    private static int MAX_OCD_SIZE = 30000;
    
    public void createAFPwithPage(AfpOutputStream out, String infile) throws IOException {

        BAG bag = new AfpBuilder().create(BAG.class);                                            // Active Environment Group

        MCF mcf = new AfpBuilder()                                            // Map coded font
                .withMember(new AfpBuilder()
                        .withMember(new AfpBuilder()
                                .with(AfplibPackage.FULLY_QUALIFIED_NAME__FQN_TYPE, FullyQualifiedNameFQNType.CONST_CODE_PAGE_NAME_REFERENCE_VALUE)
                                .with(AfplibPackage.FULLY_QUALIFIED_NAME__FQN_FORMAT, FullyQualifiedNameFQNFormat.CONST_CHARACTERSTRING_VALUE)
                                .with(AfplibPackage.FULLY_QUALIFIED_NAME__FQ_NAME, "T1000395")  // codepage
                                .create(FullyQualifiedName.class))            // Codepage reference
                        .withMember(new AfpBuilder()
                                .with(AfplibPackage.FULLY_QUALIFIED_NAME__FQN_TYPE, FullyQualifiedNameFQNType.CONST_FONT_CHARACTER_SET_NAME_REFERENCE_VALUE)
                                .with(AfplibPackage.FULLY_QUALIFIED_NAME__FQN_FORMAT, FullyQualifiedNameFQNFormat.CONST_CHARACTERSTRING_VALUE)
                                .with(AfplibPackage.FULLY_QUALIFIED_NAME__FQ_NAME, "C1N20000")  // Font
                                .create(FullyQualifiedName.class))            // Font reference
                        .withMember(new AfpBuilder()
                                .with(AfplibPackage.RESOURCE_LOCAL_IDENTIFIER__RES_TYPE, ResourceLocalIdentifierResType.CONST_CODED_FONT_VALUE)
                                .with(AfplibPackage.EXTENDED_RESOURCE_LOCAL_IDENTIFIER__RES_LID, 1) // identifier
                                .create(ResourceLocalIdentifier.class))
                        .create(MCFRG.class))
                .create(MCF.class);

        PGD pgd = new AfpBuilder()                                            // Page descriptor
                .with(AfplibPackage.PGD__XPG_BASE, PGDXpgBase.CONST10INCHES_VALUE)
                .with(AfplibPackage.PGD__YPG_BASE, PGDYpgBase.CONST10INCHES_VALUE)
                .with(AfplibPackage.PGD__XPG_UNITS, 14400)                    // 1440 dpi resolution
                .with(AfplibPackage.PGD__YPG_UNITS, 14400)                    // 1440 dpi resolution
                .with(AfplibPackage.PGD__XPG_SIZE, 11906)                    // 210 mm
                .with(AfplibPackage.PGD__YPG_SIZE, 16838)                    // 297 mm
                .with(AfplibPackage.PGD__RESERVED, 0)
                .create(PGD.class);

        PTD ptd = new AfpBuilder()                                            // Page Text Descriptor
                .with(AfplibPackage.PTD__XPBASE, PTDXPBASE.CONST_TENINCHES_VALUE)
                .with(AfplibPackage.PTD__YPBASE, PTDYPBASE.CONST_TENINCHES_VALUE)
                .with(AfplibPackage.PTD__XPUNITVL, 14400)                    // 1440 dpi resolution
                .with(AfplibPackage.PTD__YPUNITVL, 14400)                    // 1440 dpi resolution
                .with(AfplibPackage.PTD__XPEXTENT, 11906)                    // 210 mm
                .with(AfplibPackage.PTD__YPEXTENT, 16838)                    // 297 mm
                .create(PTD.class);

        EAG eag = new AfpBuilder()
                .create(EAG.class);

        BPT bpt = new AfpBuilder()
                .create(BPT.class);

        byte[] text = new String("T").getBytes(Charset.forName("IBM500")); // Encode our text in EBCDIC

        PTX ptx = new AfpBuilder()
                .withMember(new AfpBuilder()                                // Text Orientation -> Left to Right // Top to Bottom
                        .with(AfplibPackage.STO__IORNTION, STOIORNTION.CONST0DEGREES_VALUE)
                        .with(AfplibPackage.STO__BORNTION, STOBORNTION.CONST90DEGREES_VALUE)
                        .create(STO.class))
                .withMember(new AfpBuilder()
                        .with(AfplibPackage.AMB__DSPLCMNT, 1440)            // Text Cursor to 1 inch from top edge of the paper
                        .create(AMB.class))
                .withMember(new AfpBuilder()
                        .with(AfplibPackage.AMI__DSPLCMNT, 720)                // Text Cursor to 0.5 inches from left edge of the paper
                        .create(AMI.class))
                .withMember(new AfpBuilder()                                // Text Color
                        .with(AfplibPackage.SEC__COLSPCE, SECCOLSPCE.CONST_CMYK_VALUE)
                        .with(AfplibPackage.SEC__COLSIZE1, 8)
                        .with(AfplibPackage.SEC__COLSIZE2, 8)
                        .with(AfplibPackage.SEC__COLSIZE3, 8)
                        .with(AfplibPackage.SEC__COLSIZE4, 8)
                        .with(AfplibPackage.SEC__COLVALUE, new byte[] { (byte) 0x90, (byte) 0xB2, 0x00, 0x00} )
                        .create(SEC.class))
                .withMember(new AfpBuilder()
                        .with(AfplibPackage.SCFL__LID, 1)                    // identifier mapped in MCF above
                        .create(SCFL.class))
                .withMember(new AfpBuilder()
                        .with(AfplibPackage.TRN__TRNDATA, text)
                        .create(TRN.class))
                .create(PTX.class);

        EPT ept = new AfpBuilder()
                .create(EPT.class);

        EPG epg = new AfpBuilder()                                            // End Page
                .create(EPG.class);

        EDT edt = new AfpBuilder()                                            // End Document
                .create(EDT.class);

        int dpi = 360;
        int xsize = 2976;
        int ysize = 4209;
        
        System.out.println ("Reading " + infile);
        PdfReader reader = new PdfReader(infile);
        PdfDocument pdfin = new PdfDocument(reader);
        int n = pdfin.getNumberOfPages();
        
        System.out.println ("Number of pages : " + n);

        int i = 0;            
        while ( i < n ) {
            BDT     bdt = new AfpBuilder()                                            // Begin Document 
                    .with(AfplibPackage.BDT__DOC_NAME, "DOC"+i)
                    .withMember(new AfpBuilder()
                            .with(AfplibPackage.CGCSGID__GCSGID, 65535)
                            .with(AfplibPackage.CGCSGID__CPGID, 500)            // Encode all CHARs in EBCDIC
                            .create(CGCSGID.class))
                    .create(BDT.class);
            BPG bpg = new AfpBuilder()                                            // Begin Page
                    .with(AfplibPackage.BPG__PAGE_NAME, "PAGE"+i)
                    .create(BPG.class);
                        

            for(SF sf : Arrays.asList(bdt, bpg, bag, mcf, pgd, ptd, eag)) {
                out.writeStructuredField(sf);
            }            

            String outFile = infile.substring(0, infile.indexOf(".pdf")) 
            + "-" + String.format("%05d", i + 1) + ".pdf"; 

            System.out.println ("Writing " + outFile);
               PdfWriter writer = new PdfWriter(outFile);
      
            PdfDocument pdfout = new PdfDocument(writer);
            Document document = new Document(pdfout);
          
            PdfNumber pdfrotation;       

            // A4 portrait size in point = 1/72 inch - pdf standard.
            float A4height = 842.0f;
            float A4width = 595.0f;                        

            
            PdfCanvas canvas;
            PdfFormXObject page;
            Rectangle rect = new Rectangle(0,0, A4width, A4height);
            PageSize ps = new PageSize(rect);
            canvas = new PdfCanvas(pdfout.addNewPage(ps));
            
            page = pdfin.getPage(++i).copyAsFormXObject(pdfout);
            //PdfPage pagin = pdfin.getPage(i);
            //Rectangle mediabox = pagin.getMediaBox();
            //float mh = mediabox.getHeight();
            //float mw = mediabox.getWidth();
            //System.out.println("Mh="+mh);
            //System.out.println("Mw="+mw);
            int rotation;
            float height = page.getHeight();
            float width = page.getWidth();
            
            // System.out.println("height="+height);
            // System.out.println("width="+width);                        

            float scale = 1.0f; // Initialize scaling factor to 1
            float hscale;
            float wscale;         

            if (width > height) {
                wscale = A4height / width;
                hscale = A4width / height;
                rotation = 90;
            }
            else {
                wscale = A4width / width;
                hscale = A4height / height;
                rotation = 0;
            }

            if (wscale < hscale)
              scale = wscale;
            else if (hscale < wscale)
              scale = hscale;
            float [] matrix = new float[6];
            
            //System.out.println("scale="+scale);
           
     
            pdfrotation = page.getPdfObject().getAsNumber(PdfName.Rotate);
            if (pdfrotation != null) {
                rotation = pdfrotation.intValue();
                rotation = 0; // Just delete the rotation....
            }

          
            //System.out.println("rotation="+rotation);
            AffineTransform at = new AffineTransform();
            if (rotation ==0)
                at = AffineTransform.getTranslateInstance(0,0);
            else if (rotation ==90) {
               at.concatenate(AffineTransform.getRotateInstance(Math.toRadians(-rotation)));
               at.concatenate(AffineTransform.getTranslateInstance(-A4height,0));
              
            }
         
            at.concatenate(AffineTransform.getScaleInstance(scale, scale)); 
            at.getMatrix(matrix);
          
                       
            
            canvas.addXObject(page, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5] );            

                        

            // Set a watermark. Just for trials.
            Paragraph p = new Paragraph("TEST").setFontSize(60); 

            canvas.saveState();
            PdfExtGState gs1= new PdfExtGState().setFillOpacity(0.2f);
            canvas.setExtGState(gs1);
            document.showTextAligned(p, A4width/2 , A4height/2,  1, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45);
            canvas.restoreState();            
              
            page.put(PdfName.Rotate, new PdfNumber(0)); // always portrait
            document.flush();
            document.close();
            pdfout.close();          
            canvas.release();
            writer.close();
            InputStream fin = new BufferedInputStream(new FileInputStream(outFile));

            pdfoc(dpi, xsize, ysize, fin, out, "PAGE"+i);  // Create a afp document per pdf.
            
            for(SF sf : Arrays.asList(bpt, ptx, ept, epg, edt)) {
                out.writeStructuredField(sf);
            }
            // Delete file or memory only - To do: don't use intermediate file
            fin.close();
        }
          pdfin.close();        

    }

    private void pdfoc(int dpi, int xsize, int ysize, InputStream fin, AfpOutputStream out, String ovlName)
            throws IOException {

        out.writeStructuredField(new AfpBuilder()
                .withMember(new AfpBuilder()
                        .with(AfplibPackage.OBJECT_CLASSIFICATION__OBJ_CLASS, ObjectClassificationObjClass.CONST_TIMEINVARIANT_VALUE)
                        .with(AfplibPackage.OBJECT_CLASSIFICATION__STRUC_FLGS, 64512)
                        .with(AfplibPackage.OBJECT_CLASSIFICATION__REG_OBJ_ID, new byte[] {0x06, 0x07, 0x2b, 0x12, 0x00, 0x04, 0x01, 0x01, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})
                        .create(ObjectClassification.class))
                .with(AfplibPackage.BOC__OBJ_CNAME, ovlName)
                .create(BOC.class));

        out.writeStructuredField(new AfpBuilder()
            .create(BOG.class));

 

        out.writeStructuredField(new AfpBuilder()
            .withMember(new AfpBuilder()
                .with(AfplibPackage.DESCRIPTOR_POSITION__DES_POS_ID, 1)
                .create(DescriptorPosition.class))

            .withMember(new AfpBuilder()
                .with(AfplibPackage.MEASUREMENT_UNITS__XOA_BASE, MeasurementUnitsXoaBase.CONST10INCHES_VALUE)
                .with(AfplibPackage.MEASUREMENT_UNITS__YOA_BASE, MeasurementUnitsYoaBase.CONST10INCHES_VALUE)
                .with(AfplibPackage.MEASUREMENT_UNITS__XOA_UNITS, dpi*10)
                .with(AfplibPackage.MEASUREMENT_UNITS__YOA_UNITS, dpi*10)
                .create(MeasurementUnits.class))
            .withMember(new AfpBuilder()
                .with(AfplibPackage.OBJECT_AREA_SIZE__SIZE_TYPE, ObjectAreaSizeSizeType.CONST_OBJECT_AREA_SIZE_VALUE)
                .with(AfplibPackage.OBJECT_AREA_SIZE__XOA_SIZE, xsize)
                .with(AfplibPackage.OBJECT_AREA_SIZE__YOA_SIZE, ysize)
                .create(ObjectAreaSize.class))
            .create(OBD.class));

        out.writeStructuredField(new AfpBuilder()
            .with(AfplibPackage.OBP__OA_POS_ID, 1)
            .with(AfplibPackage.OBP__RG_LENGTH, OBPRGLength.CONST_LENGTH23_VALUE)
            .with(AfplibPackage.OBP__XOA_OSET, 0)
            .with(AfplibPackage.OBP__YOA_OSET, 0)
            .with(AfplibPackage.OBP__XOA_ORENT, OBPXoaOrent.CONST0DEGREES_VALUE)
            .with(AfplibPackage.OBP__YOA_ORENT, OBPYoaOrent.CONST90DEGREES_VALUE)
            .with(AfplibPackage.OBP__XOCA_OSET, 0)
            .with(AfplibPackage.OBP__YOCA_OSET, 0)
            .with(AfplibPackage.OBP__XOCA_ORENT, OBPXocaOrent.CONST0DEGREES_VALUE)
            .with(AfplibPackage.OBP__YOCA_ORENT, OBPYocaOrent.CONST90DEGREES_VALUE)
            .with(AfplibPackage.OBP__REF_CSYS, OBPRefCSys.CONST_PAGE_OR_OVERLAY_VALUE)
            .create(OBP.class));

        out.writeStructuredField(new AfpBuilder()
            .withMember(new AfpBuilder()
                .withMember(new AfpBuilder()
                    .with(AfplibPackage.MAPPING_OPTION__MAP_VALUE, MappingOptionMapValue.CONST_POSITIONANDTRIM_VALUE)
                    .create(MappingOption.class))
                .create(MCDRG.class))
            .create(MCD.class));

        out.writeStructuredField(new AfpBuilder()
            .with(AfplibPackage.CDD__XOC_BASE, CDDXocBase.CONST10IN_VALUE)
            .with(AfplibPackage.CDD__YOC_BASE, CDDYocBase.CONST10IN_VALUE)
            .with(AfplibPackage.CDD__XOC_UNITS, dpi * 10)
            .with(AfplibPackage.CDD__YOC_UNITS, dpi * 10)
            .with(AfplibPackage.CDD__XOC_SIZE, xsize)
            .with(AfplibPackage.CDD__XOC_SIZE, ysize)
            .create(CDD.class));

        out.writeStructuredField(new AfpBuilder()
            .create(EOG.class));

        byte[] buffer = new byte[MAX_OCD_SIZE];
        int l;
        while ((l = fin.read(buffer)) > 0) {
            OCD ocd = AfplibFactory.eINSTANCE.createOCD();
            byte[] c = new byte[l];
            System.arraycopy(buffer, 0, c, 0, l);
            ocd.setObjCdat(c);
            out.writeStructuredField(ocd);
        }

        out.writeStructuredField(new AfpBuilder()
            .with(AfplibPackage.EOC__OBJ_CNAME, ovlName)
            .create(EOC.class));
    }

    public static void main(String[] args) {
        try {
             String inFile = args[0];
             String outFile = inFile.substring(0, inFile.indexOf(".pdf")) + ".afp";
                      
             new ConvertPDFToAFP().createAFPwithPage(new AfpOutputStream(new FileOutputStream(outFile)), inFile);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
