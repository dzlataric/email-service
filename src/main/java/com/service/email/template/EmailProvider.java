package com.service.email.template;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public final class EmailProvider {

	private static final String USERNAME = "xxxxxx@gmail.com";
	private static final String PASSWORD = "xxxxxxxxxxxx";

	public static void sendBasicEmail(final String sender, final String receiver, final String subject,
			final String text) {
		MimeMessage message = new MimeMessage(prepareSession());
		try {
			message.setSender(new InternetAddress(USERNAME));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			message.setSubject(subject);
			message.setContent("<h1>" + text + "</h1>", "text/html");
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendReportEmail(final String sender, final String receiver, final String subject,
			final String text) {

		ByteArrayOutputStream outputStream = null;
		try {
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(new StringBuilder().append("Sender: " + sender + "\n").append(text).toString());

			outputStream = new ByteArrayOutputStream();
			writePdf(outputStream);
			byte[] bytes = outputStream.toByteArray();

			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(bytes, "application/pdf")));
			pdfBodyPart.setFileName("Period report.pdf");

			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);

			MimeMessage mimeMessage = new MimeMessage(prepareSession());
			mimeMessage.setSender(new InternetAddress(USERNAME));
			mimeMessage.setSubject(subject);
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			mimeMessage.setContent(mimeMultipart);

			Transport.send(mimeMessage);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static Session prepareSession() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", 587);
		properties.put("mail.username", USERNAME);
		properties.put("mail.password", PASSWORD);
		properties.put("mail.smtp.starttls.enable", true);
		properties.put("mail.smtp.starttls.required", true);
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.connectiontimeout", 5000);
		properties.put("mail.smtp.timeout", 5000);
		properties.put("mail.smtp.writetimeout", 5000);
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});
		return session;
	}

	private static void writePdf(OutputStream outputStream) throws Exception {
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);

		document.open();

		document.addTitle("Period report");

		Paragraph titleParagraph = new Paragraph();
		titleParagraph.setAlignment(Element.ALIGN_CENTER);
		titleParagraph.setFont(FontFactory.getFont("Arial", 32));
		titleParagraph.add(new Chunk("Generated period report"));
		titleParagraph.setSpacingAfter(30);

		Paragraph datesParagraph = new Paragraph();
		datesParagraph.setAlignment(Element.ALIGN_CENTER);
		datesParagraph.setFont(FontFactory.getFont("Arial", 24));
		datesParagraph.add(new Chunk("Period: " + new SimpleDateFormat("EEE, dd MMM, yyyy").format(new Date()) + " - "
				+ new SimpleDateFormat("EEE, dd MMM, yyyy").format(new Date())));
		datesParagraph.setSpacingAfter(30);

		PdfPTable table = new PdfPTable(8);
		for (int aw = 0; aw < 8; aw++) {
			table.addCell("hello");
		}

		document.add(titleParagraph);
		document.add(datesParagraph);
		document.add(table);

		document.close();
	}

}
