/**
 * 
 */
package com.murphy.taskmgmt.dto;

import java.util.List;

/**
 * @author Kamlesh.Choubey
 *
 */
public class SsdBypassHeaderResponseDto {
	
	private SsdBypassHeaderDto ssdBypassHeaderDto;
	private List<SsdBypassCommentDto> ssdBypassCommentDto;
	private List<SsdBypassAttachementDto> ssdBypassAttachementDto;
	private List<SsdBypassActivityLogDto> ssdBypassActivityLogDto;
	/**
	 * @return the ssdBypassHeaderDto
	 */
	public SsdBypassHeaderDto getSsdBypassHeaderDto() {
		return ssdBypassHeaderDto;
	}
	/**
	 * @param ssdBypassHeaderDto the ssdBypassHeaderDto to set
	 */
	public void setSsdBypassHeaderDto(SsdBypassHeaderDto ssdBypassHeaderDto) {
		this.ssdBypassHeaderDto = ssdBypassHeaderDto;
	}
	/**
	 * @return the ssdBypassCommentDto
	 */
	public List<SsdBypassCommentDto> getSsdBypassCommentDto() {
		return ssdBypassCommentDto;
	}
	/**
	 * @param ssdBypassCommentDto the ssdBypassCommentDto to set
	 */
	public void setSsdBypassCommentDto(List<SsdBypassCommentDto> ssdBypassCommentDto) {
		this.ssdBypassCommentDto = ssdBypassCommentDto;
	}
	/**
	 * @return the ssdBypassAttachementDto
	 */
	public List<SsdBypassAttachementDto> getSsdBypassAttachementDto() {
		return ssdBypassAttachementDto;
	}
	/**
	 * @param ssdBypassAttachementDto the ssdBypassAttachementDto to set
	 */
	public void setSsdBypassAttachementDto(List<SsdBypassAttachementDto> ssdBypassAttachementDto) {
		this.ssdBypassAttachementDto = ssdBypassAttachementDto;
	}
	/**
	 * @return the ssdBypassActivityLogDto
	 */
	public List<SsdBypassActivityLogDto> getSsdBypassActivityLogDto() {
		return ssdBypassActivityLogDto;
	}
	/**
	 * @param ssdBypassActivityLogDto the ssdBypassActivityLogDto to set
	 */
	public void setSsdBypassActivityLogDto(List<SsdBypassActivityLogDto> ssdBypassActivityLogDto) {
		this.ssdBypassActivityLogDto = ssdBypassActivityLogDto;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SsdBypassHeaderResponseDto [ssdBypassHeaderDto=" + ssdBypassHeaderDto + ", ssdBypassCommentDto="
				+ ssdBypassCommentDto + ", ssdBypassAttachementDto=" + ssdBypassAttachementDto
				+ ", ssdBypassActivityLogDto=" + ssdBypassActivityLogDto + "]";
	}
	

	
}
