package com.incture.iopptw.template.dtos;

import com.incture.iopptw.dtos.JsaheaderDto;
import com.incture.iopptw.dtos.JsappeDto;

import lombok.Data;

@Data
public class CreateTemplateDto {
	private JsaheaderDto jsaheaderDto;
	private JsappeDto jsappeDto;
}
