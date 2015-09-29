package export;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import application.AppController;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class SvgToPdf {

	protected SAXSVGDocumentFactory factory;

	protected BridgeContext ctx;

	protected GVTBuilder builder;

	public SvgToPdf() {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		factory = new SAXSVGDocumentFactory(parser);

		UserAgent userAgent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(userAgent);
		ctx = new BridgeContext(userAgent, loader);
		ctx.setDynamicState(BridgeContext.DYNAMIC);

		builder = new GVTBuilder();
	}

	@SuppressWarnings("deprecation")
	public void drawSvg(PdfTemplate map, String resource) throws IOException {
		Graphics2D g2d = new PdfGraphics2D(map, AppController.WIDTH + 50, AppController.HEIGHT + 50);
		SVGDocument city = factory.createSVGDocument(new File(resource).toURL().toString());
		GraphicsNode mapGraphics = builder.build(ctx, city);
		mapGraphics.paint(g2d);
		g2d.dispose();
	}

	public void createPdf(String svg, String pdf) throws IOException, DocumentException {
		Document document = new Document(new Rectangle(AppController.WIDTH + 100, AppController.HEIGHT + 100));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdf));
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfTemplate map = cb.createTemplate(AppController.WIDTH + 100, AppController.HEIGHT + 100);
		drawSvg(map, svg);
		cb.addTemplate(map, 0, 0);
		document.close();
	}

}