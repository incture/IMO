package com.murphy.taskmgmt.dao;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gnostice.pdfone.PdfDocument;
import com.gnostice.pdfone.PdfSearchElement;
import com.gnostice.pdfone.PdfSearchMode;
import com.gnostice.pdfone.PdfSearchOptions;
import com.murphy.taskmgmt.dto.HseDocumentDto;
import com.murphy.taskmgmt.dto.HseDocumentResponse;
import com.murphy.taskmgmt.dto.HseResponseBodyDto;
import com.murphy.taskmgmt.dto.HseStringList;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.entity.HseStringsDo;
import com.murphy.taskmgmt.exception.ExecutionFault;
import com.murphy.taskmgmt.exception.InvalidInputFault;
import com.murphy.taskmgmt.exception.NoResultFault;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.murphy.taskmgmt.util.ServicesUtil;

@Repository("HSEStringDao")
public class HseStringDao extends BaseDao<HseStringsDo, HseDocumentDto> {
	private static final Logger logger = LoggerFactory.getLogger(HseStringDao.class);
	@Autowired
	HseDocumentDao documentDao;
	@Override
	protected HseStringsDo importDto(HseDocumentDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		HseStringsDo entity = new HseStringsDo();

		if (!ServicesUtil.isEmpty(fromDto.getStringId()))
			entity.setId((fromDto.getStringId()));

		if (!ServicesUtil.isEmpty(fromDto.getSearchString()))
			entity.setSearchString((fromDto.getSearchString()));

		if (!ServicesUtil.isEmpty(fromDto.getStringCount()))
			entity.setCount((fromDto.getStringCount()));

		return entity;

	}

	@Override
	protected HseDocumentDto exportDto(HseStringsDo entity) {
		HseDocumentDto dto = new HseDocumentDto();
		if (!ServicesUtil.isEmpty(entity.getId()))
			dto.setStringId(entity.getId());

		if (!ServicesUtil.isEmpty(entity.getSearchString()))
			dto.setSearchString(entity.getSearchString());

		if (!ServicesUtil.isEmpty(entity.getCount()))
			dto.setStringCount(entity.getCount());

		return dto;
	}

	@SuppressWarnings({ "unchecked", "deprecation", "unused", "rawtypes" })
	public HseDocumentResponse insertIntoTable(HseDocumentDto dto) {
		HseDocumentResponse responseDto = new HseDocumentResponse();
		HseStringList hseStringListDto = null;
		List<HseStringList> stringList = new ArrayList<HseStringList>();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(MurphyConstant.READ_FAILURE);
		int count = 1;
		int result = 0;

		String dtoText = dto.getSearchString();
		try {
			String q = "select ID,SEARCH_STRING, STRING_COUNT from HSE_STRINGS where lower(SEARCH_STRING) = " + "'"
					+ dto.getSearchString() + "'";
			Query executeQuery = this.getSession().createSQLQuery(q);
			logger.error("[Murphy][HseStringDao][insertIntoTable][EXECUTE QUERY]" + executeQuery);

			List<Object[]> response = executeQuery.list();

			if (!ServicesUtil.isEmpty(response)) {
				HseDocumentDto hseDto = null;
				for (Object[] obj : response) {
					hseDto = new HseDocumentDto();
					hseDto.setStringId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					hseDto.setSearchString(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
					hseDto.setStringCount(ServicesUtil.isEmpty(obj[2]) ? null : (Integer) obj[2]);

					String text = hseDto.getSearchString();
					logger.error("[Murphy][HseStringDao][insertIntoTable][TEXT]" + text);

					// if it exist then update its count by 1.

					if (dtoText.equalsIgnoreCase(text)) {
						int stringCount = hseDto.getStringCount();
						int newstringCount = stringCount + 1;
						logger.error("[Murphy][HseStringDao][insertIntoTable][STRING COUNT]" + stringCount);
						logger.error("[Murphy][HseStringDao][insertIntoTable][ NEW STRING COUNT]" + newstringCount);
						try {
							int updateTable = documentDao.updateTable(text, newstringCount);
							if (result > 0) {
								responseMessage.setStatus(MurphyConstant.SUCCESS);
								responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
								logger.error("result>0 " + result);
							} else {
								logger.error("result0 " + result);
							}

						} catch (Exception e) {
							logger.error("[Murphy][HseStringDao][insertIntoTable][ERROR IN UPDATE QUERY]" + e);
						}
					}

				}

			} else {
				HseDocumentDto newDto = new HseDocumentDto();
				/*String decodedValue = "";
				try {
					
					try {

						decodedValue = URLDecoder.decode(dtoText, "UTF-8");
						logger.error("[Murphy][HseStringDao][insertIntoTable][Decoded value is: ]" + decodedValue);
					} catch (UnsupportedEncodingException uee) {
						logger.error("[Murphy][HseStringDao][insertIntoTable][Decoded value error]" + uee.getMessage());
					}
*/
					newDto.setSearchString(dtoText);
					newDto.setStringCount(count);
					logger.error("[Murphy][HseStringDao][insertIntoTable][New String Count]" + newDto.getStringCount());
					create(newDto);
				
			}

			// GET THE DETAILS OF THE STRING PRESENT IN THE DOCUMENT

			int totalPages = 0;
			String matchedString = null;
			int pageNumber = 0;
			String lineContainingString = null;
			int totalOccur = 0;
			String newLine = "";

			try {

				String urlquery = " select hse.URL , hse.VERSION" + " from HSE_DOCUMENTS hse "				
						+ " ORDER BY version DESC LIMIT 1 ";

				Query executeQ = this.getSession().createSQLQuery(urlquery);
				logger.error("[Murphy][TaskEventsDao][insertIntoTable][query]" + executeQ);

				List<Object[]> resp = executeQ.list();
				HseDocumentDto hseDocumentdto = null;
				if (!ServicesUtil.isEmpty(resp)) {
					for (Object[] obj : resp) {

						hseDocumentdto = new HseDocumentDto();
						hseDocumentdto.setAttachmentUrl(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
						logger.error("[Murphy][HseDocumentDao][insertIntoTable][DocumentURL]"
								+ hseDocumentdto.getAttachmentUrl());
						hseDocumentdto.setDocVersion(ServicesUtil.isEmpty(obj[1]) ? null : (Integer) obj[1]);
						logger.error("[Murphy][HseDocumentDao][insertIntoTable][DocumentVersion]"
								+ hseDocumentdto.getDocVersion());
					}

				}

				String latestUrl = hseDocumentdto.getAttachmentUrl();

				URL url1 = new URL(latestUrl);
				logger.error("[Murphy][HseDocumentDao][insertIntoTable][DocumentURL FROM URL.NET]" + url1);
				byte[] ba1 = new byte[1024];
				int baLength;
				FileOutputStream fos1 = new FileOutputStream("download.pdf");
				try {
					// Contacting the URL
					com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
					HttpURLConnection httpconn = (HttpURLConnection) url1.openConnection();
					if (!ServicesUtil.isEmpty(httpconn)) {

						int statusCode = httpconn.getResponseCode();
						logger.error(
								"[Murphy][HseDocumentDao][insertIntoTable][URL Connection][STATUS CODE]" + statusCode);

						try {

							// Read the PDF from the URL and save to a local
							// file
							InputStream is1 = url1.openStream();

							while ((baLength = is1.read(ba1)) != -1) {
								fos1.write(ba1, 0, baLength);
							}

							fos1.flush();
							fos1.close();
							is1.close();

							// Load the PDF document and display its page
							// count
							logger.error("[Murphy][HseDocumentDao][insertIntoTable][DONE.Processing the PDF ...] ");
							PdfDocument doc = new PdfDocument();
							try {
								doc.load("download.pdf");

								logger.error(
										"[Murphy][HseDocumentDao][insertIntoTable][DONE.Number of pages in the PDF is ]"
												+ doc.getPageCount());

								int i, n, pageCount;

								PdfSearchElement pseResult;

								// Iterate through all search results
								pageCount = doc.getPageCount();
								responseDto.setTotalPages(pageCount);
								totalPages = responseDto.getTotalPages();

								for (int j = 1; j <= pageCount; j++) {
									hseStringListDto = new HseStringList();

									ArrayList lstSearchResults1 = (ArrayList) doc.search(dtoText, j,
											PdfSearchMode.LITERAL, PdfSearchOptions.NONE);

									n = lstSearchResults1.size();
									totalOccur = totalOccur + n;
									logger.error("[Murphy][HseStringDao][insertIntoTable][string occurence in page ]"
											+ n + "[ On Page number]" + j);
									for (i = 0; i < n; i++) {
										hseStringListDto = new HseStringList();

										pseResult = (PdfSearchElement) lstSearchResults1.get(i);

										hseStringListDto.setSearchedText(pseResult.getMatchString());
										matchedString = hseStringListDto.getSearchedText();
										
										lineContainingString = pseResult.getLineContainingMatchString();
										
										hseStringListDto.setLine(lineContainingString);
										
										String content = lineContainingString;
										String regex    =   "^x";
										
										Pattern pattern =   Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
										
										Matcher matcher =   pattern.matcher(content);
										
										while (matcher.find())
										{
											newLine= matcher.replaceFirst("");	
											newLine= newLine.replaceFirst( "\\s+", "");
											hseStringListDto.setLine(newLine);
											logger.error("[Murphy][HseStringDao][insertIntoTable][lineContainingString]"
													+ newLine);
										}
										hseStringListDto.setPageNumber(pseResult.getPageNum());
										pageNumber = hseStringListDto.getPageNumber();
										hseStringListDto.setStringCount(i + 1);

										logger.error("[Murphy][HseStringDao][insertIntoTable][Matched String]"
												+ matchedString);
										logger.error("[Murphy][HseStringDao][insertIntoTable][lineContainingString]"
												+ lineContainingString);
										logger.error(
												"[Murphy][HseStringDao][insertIntoTable][pageNumber]" + pageNumber);
										stringList.add(hseStringListDto);
									}
									responseDto.setHseStringList(stringList);
								}

								responseDto.setTotalOccurenceOfString(totalOccur);
								doc.close();

							} catch (Exception e) {
								logger.error(
										"[Murphy][HseStringDao][error while fetching string details from document][error]"
												+ e.getMessage());
							}
						} catch (Exception e) {
							logger.error(
									"[Murphy][HseStringDao][error while fetching string details from document][error]"
											+ e.getMessage());
						}
						// }
					} else {
						logger.error("[Murphy][HseStringDao][HTTP CONNECTION IS NOT ESTABLISHED][error]");
					}
				} catch (Exception e) {
					logger.error("[Murphy][HseStringDao][error while processing the document][error]" + e.getMessage());
				}

			} catch (Exception e) {
				logger.error(
						"[Murphy][HseStringDao][error while fetching string from document][error]" + e.getMessage());
			}

			responseMessage.setStatus(MurphyConstant.SUCCESS);
			responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);
		} catch (Exception e) {
			logger.error("[Murphy][HseStringDao][insertIntoTable][error]" + e.getMessage());
		}
		responseDto.setResponseMessage(responseMessage);
		return responseDto;

	}

	// GET PARAGRAPH
		public HseDocumentResponse getParagraph(int pageNumber, String matchedLine, String latestUrl) {
			String paragraph1 = null;
			HseDocumentResponse responseDto = new HseDocumentResponse();
			ResponseMessage responseMessage = new ResponseMessage();
			HseStringList hseStringListDto = null;
			List<HseStringList> stringList = new ArrayList<HseStringList>();
			String text = matchedLine;
			int count = 0;
			try {

				responseMessage.setMessage(MurphyConstant.READ_FAILURE);
				URL url1 = new URL(latestUrl);
				HttpURLConnection httpconn = (HttpURLConnection) url1.openConnection();
				logger.error("[Murphy][HseStringDao][getParagraph][Response code]" + httpconn.getResponseCode());
				InputStream is1 = httpconn.getInputStream();
				PDDocument doc = PDDocument.load(is1);
				PDFTextStripper stripper = new PDFTextStripper();
				int noOfPages = doc.getNumberOfPages();
				logger.error("[Murphy][HseStringDao][getParagraph][Total number of Page]" + noOfPages);

				for (int i = pageNumber; i <= pageNumber; i++) {
					stripper.setStartPage(pageNumber);
					stripper.setEndPage(pageNumber);
					stripper.setParagraphStart("/t");
					stripper.setSortByPosition(true);
					hseStringListDto = new HseStringList();
					logger.error("[Murphy][HseStringDao][getParagraph][Page Number]" + pageNumber);
					for (String paragraph : stripper.getText(doc).split(stripper.getParagraphStart())) {
						hseStringListDto = new HseStringList();
						if (paragraph.contains(text)) {
							paragraph1 = paragraph;
							paragraph1 = paragraph1.replaceAll("\\r\\n|\\n", "");
							hseStringListDto.setParagraph(paragraph1);
							logger.error("[Murphy][HseStringDao][getParagraph][Paragraph] " + paragraph1);
							stringList.add(hseStringListDto);
							count = count + 1;

							logger.error("[Murphy][HseStringDao][getParagraph][Count of  PARAGRAPH in page] " + count);

						}
						if (count==1) {
							HseStringList para = stringList.get(count - 1);
							String getPara = para.getParagraph();
							responseDto.setParagraph(getPara);
							break;
						} 
					}

				}

				responseMessage.setStatus(MurphyConstant.SUCCESS);
				responseMessage.setStatusCode(MurphyConstant.CODE_SUCCESS);

			} catch (Exception e) {
				logger.error("[Murphy][HseStringDao][getParagraph][error]" + e.getMessage());
			}
			responseDto.setResponseMessage(responseMessage);
			return responseDto;
		}
	@SuppressWarnings("unchecked")
	public HseResponseBodyDto displayStringList() {
		String q = "select ID,SEARCH_STRING, STRING_COUNT from HSE_STRINGS where "
				+ "SEARCH_STRING not in('JSA','jsa','PPE','ppe','H2S','h2s','EXCAVATION','Excavation','excavation', 'Hot Work', 'hot work') "
				+ "order by STRING_COUNT desc limit 5";
		Query executeQuery = this.getSession().createSQLQuery(q);
		logger.error("[Murphy][HseStringDao][insertIntoTable][EXECUTE QUERY]" + executeQuery);
		List<Object[]> response = executeQuery.list();
		HseResponseBodyDto responseDto = null;
		List<String> topSearches = documentDao.topSearhes();
		List<String> searchStrings = new ArrayList<>();
		searchStrings.addAll(topSearches);
		if (!ServicesUtil.isEmpty(response)) {
			HseDocumentDto hseDto = null;
			for (Object[] obj : response) {
				responseDto = new HseResponseBodyDto();
				hseDto = new HseDocumentDto();
				hseDto.setStringId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
				hseDto.setSearchString(ServicesUtil.isEmpty(obj[1]) ? null : (String) obj[1]);
				hseDto.setStringCount(ServicesUtil.isEmpty(obj[2]) ? null : (Integer) obj[2]);
				searchStrings.add(hseDto.getSearchString());
				logger.error("[Murphy][HseStringDao][insertIntoTable][EXECUTE QUERY TOP SEARCHES]" + searchStrings);
			}
		}
		responseDto.setStringList(searchStrings);
		return responseDto;

	}

}
