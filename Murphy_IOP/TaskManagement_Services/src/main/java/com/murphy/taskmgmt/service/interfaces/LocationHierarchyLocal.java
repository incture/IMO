package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import com.murphy.taskmgmt.dto.FieldResponseDto;
import com.murphy.taskmgmt.dto.HierarchyRequestDto;
import com.murphy.taskmgmt.dto.LocationHierarchyDto;
import com.murphy.taskmgmt.dto.LocationHierarchyResponseDto;
import com.murphy.taskmgmt.dto.LocationResponseDto;

public interface LocationHierarchyLocal {

	LocationResponseDto getHierarchy(HierarchyRequestDto dto);

	FieldResponseDto getFeild(String loc, String locType);

	LocationHierarchyDto getWellDetails(String muwi);

	LocationHierarchyResponseDto getParentByCompressor(String compressorId);

	FieldResponseDto getFieldTextForLocCode(List<String> location, String locType);

}
