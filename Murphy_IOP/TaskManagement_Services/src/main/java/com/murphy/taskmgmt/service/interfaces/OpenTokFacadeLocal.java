/**
 * 
 */
package com.murphy.taskmgmt.service.interfaces;

import com.murphy.taskmgmt.dto.OpenTokDto;
import com.murphy.taskmgmt.dto.OpenTokResponseDto;

/**
 * @author Kamlesh.Choubey
 *
 */
public interface OpenTokFacadeLocal {
	public OpenTokResponseDto createCall(OpenTokDto openTokDto);
	public String updateARCallResponse(OpenTokDto openTokDto , String responseBy , String actionType , String sendNotification);

}
