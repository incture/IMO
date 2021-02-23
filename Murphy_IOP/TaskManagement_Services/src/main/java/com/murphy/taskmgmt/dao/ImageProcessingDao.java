package com.murphy.taskmgmt.dao;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.murphy.taskmgmt.dto.AttachmentDto;
import com.murphy.taskmgmt.entity.AttachmentDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;

@Repository("ImageProcessingDao")
public class ImageProcessingDao extends BaseDao<AttachmentDo, AttachmentDto> {

	private static final Logger logger = LoggerFactory.getLogger(CollaborationDao.class);

	public ImageProcessingDao() {
	}

	@Override
	protected AttachmentDo importDto(AttachmentDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		return null;
	}

	@Override
	protected AttachmentDto exportDto(AttachmentDo entity) {
		return null;
	}

	static String ImageFormat = "";

	public String getCompressedImage(String base64, String fileType) throws IOException {

		double percent = 0.1;
		String comppressedScaled = "";
		if (MurphyConstant.FILE_TYPE_PNG.equals(fileType))
			ImageFormat = "png";
		else if (MurphyConstant.FILE_TYPE_JPG.equals(fileType))
			ImageFormat = "jpg";
		else if (MurphyConstant.FILE_TYPE_JPEG.equals(fileType))
			ImageFormat = "jpeg";
		try {
			File input = convertBase64ToFile(base64);
			logger.error("[MURPHY][ImageProcessing Image][getCompressedImage]" + input);
			BufferedImage image = ImageIO.read(input);

			if (!MurphyConstant.FILE_TYPE_PNG.equals(fileType))
				compressImage(image, input);
			scaleImage(input, percent, image);
			comppressedScaled = convertFileToBase64(input);
			//logger.error("[MURPHY][ImageProcessing Image][getCompressedImage][base64 being saved]" + comppressedScaled);
		} catch (Exception e) {
			logger.error("[MURPHY][ImageProcessing Image][getCompressedImage][Error]:" + e);
		}
		return comppressedScaled;
	}

	public static String convertFileToBase64(File inpFile) throws FileNotFoundException, IOException {
		try {
			byte[] encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(inpFile));
			return new String(encoded, StandardCharsets.US_ASCII);
		} catch (Exception e) {
			logger.error("[MURPHY][ImageProcessing Image][convertFileToBase64][Error]:" + e);
		}
		return null;
	}

	public static File convertBase64ToFile(String base64) throws FileNotFoundException, IOException {
		try {
			File file = File.createTempFile("NEW", "." + ImageFormat);
			byte[] data = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
//			byte[] data =  base64.getBytes(StandardCharsets.UTF_8);
			try (OutputStream stream = new FileOutputStream(file)) {
				stream.write(data);
			}
			return file;
		} catch (Exception e) {
			logger.error("[MURPHY][ImageProcessing Image][convertBase64ToFile][Error]:" + e);
		}
		return null;
	}

	public static void compressImage(BufferedImage image, File inputFile) {
		try {
			OutputStream os = new FileOutputStream(inputFile);

			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(ImageFormat);
			ImageWriter writer = (ImageWriter) writers.next();

			ImageOutputStream ios = ImageIO.createImageOutputStream(os);
			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.10f);
			writer.write(null, new IIOImage(image, null, null), param);

			os.close();
			ios.close();
			writer.dispose();
		} catch (Exception e) {
			logger.error("[MURPHY][ImageProcessing Image][compressImage][Error]:" + e);
		}
	}

	public static void scaleImage(File inputImagePath, double percent, BufferedImage inputImage) throws IOException {
		try {
			int scaledWidth = (int) (inputImage.getWidth() * percent);
			int scaledHeight = (int) (inputImage.getHeight() * percent);

			BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
			Graphics2D g2d = outputImage.createGraphics();
			g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
			g2d.dispose();
			ImageIO.write(outputImage, ImageFormat, inputImagePath);
		} catch (Exception e) {
			logger.error("[MURPHY][ImageProcessing Image][scaleImage][Error]:" + e);
		}
	}

}
