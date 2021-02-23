package com.murphy.taskmgmt.service.interfaces;

import java.util.List;

import com.murphy.taskmgmt.dto.HandoverNotesDto;
import com.murphy.taskmgmt.dto.HandoverNotesRequestDto;
import com.murphy.taskmgmt.dto.HandoverNotesToROCResponseDto;
import com.murphy.taskmgmt.dto.ResponseMessage;

public interface HandoverNotesToROCFacadeLocal {

	ResponseMessage saveOrUpdateHandoverNotes(List<HandoverNotesDto> handoverNotesDtoList);
	HandoverNotesToROCResponseDto getNotesForTheNoteIds(Integer noteId);
	HandoverNotesToROCResponseDto getNoteByNoteType(HandoverNotesRequestDto requestDto);
	HandoverNotesToROCResponseDto getDefaultNotes(HandoverNotesDto handOverNotesDto);

}
