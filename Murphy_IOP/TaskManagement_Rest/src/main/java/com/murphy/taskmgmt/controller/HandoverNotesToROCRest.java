package com.murphy.taskmgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murphy.taskmgmt.dto.HandoverNotesDto;
import com.murphy.taskmgmt.dto.HandoverNotesRequestDto;
import com.murphy.taskmgmt.dto.HandoverNotesToROCResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;
import com.murphy.taskmgmt.service.interfaces.HandoverNotesToROCFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("com.murphy")
@RequestMapping(value = "/handoverNotesTOROC", produces = "application/json")
public class HandoverNotesToROCRest {
	
	
	@Autowired
	HandoverNotesToROCFacadeLocal facadeLocal;
	
	
	@RequestMapping(value = "/saveOrUpdateHandoverNotes", method = RequestMethod.POST)
	public ResponseMessage saveOrUpdateHandoverNotes(@RequestBody List<HandoverNotesDto> handoverNotesDtoList)
	{
		return facadeLocal.saveOrUpdateHandoverNotes(handoverNotesDtoList);
	}
	
	@RequestMapping(value = "/getNotesForNoteId", method = RequestMethod.GET)
	public HandoverNotesToROCResponseDto getNotesForTheNoteIds(@RequestParam("noteId") Integer noteId)
	{
		return facadeLocal.getNotesForTheNoteIds(noteId);
	}
	
	@RequestMapping(value = "/getNotesByNoteType", method = RequestMethod.POST)
	public HandoverNotesToROCResponseDto getNoteByNoteType(@RequestBody HandoverNotesRequestDto requestDto)
	{
		return facadeLocal.getNoteByNoteType(requestDto);
	}
	
	@RequestMapping(value = "/getNotedetails", method = RequestMethod.POST)
	public HandoverNotesToROCResponseDto getDefaultNotes(@RequestBody HandoverNotesDto handOverNotesDto)
	{
		return facadeLocal.getDefaultNotes(handOverNotesDto);
	}

}
