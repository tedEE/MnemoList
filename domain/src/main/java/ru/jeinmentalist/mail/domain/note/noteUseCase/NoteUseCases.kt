package ru.jeinmentalist.mail.domain.note.noteUseCase

data class NoteUseCases (
    val addNote: AddNoteUseCase,
    val getNoteById: GetNoteByIdUseCase,
    val getNoteByProfileId: GetNoteByProfileIdUseCase,
    val getNoteList: GetNoteListUseCase,
    val updateNext: UpdateNoteNextTimestampUseCase,
    val deleteNote: DeleteNoteUseCase,
    val getNotesFlow: GetNotesFlowUseCase
)