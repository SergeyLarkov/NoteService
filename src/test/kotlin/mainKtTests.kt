import org.junit.Test

import org.junit.Assert.*

class mainKtTests {
    @Test
    fun test_addNote() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        assertFalse(noteService.add("test", "test") == 0)
    }

    @Test
    fun test_deleteNote() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        assertTrue(noteService.delete(noteId))
    }

    @Test
    fun test_errorDeleteNoteById() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        assertFalse(noteService.delete(noteId + 1))
    }

    @Test
    fun test_errorDeleteNoteByOwner() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        noteService.userId = 2
        assertFalse(noteService.delete(noteId))
    }

    @Test
    fun test_editNote() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        assertTrue(noteService.edit(noteId, "new title", "new text"))
    }

    @Test
    fun test_errorEditNoteById() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        assertFalse(noteService.edit(noteId + 1, "new title", "new text"))
    }

    @Test
    fun test_errorEditNoteByOwner() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        noteService.userId = 2
        assertFalse(noteService.edit(noteId, "new title", "new text"))
    }

    @Test
    fun test_getById() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        assertTrue(noteService.getById(noteId).id == noteId)
    }

    @Test(expected = RuntimeException::class)
    fun test_getById_shouldThrowException() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val note = noteService.getById(999)
    }

    @Test
    fun test_createComment() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        assertFalse(noteService.createComment(noteId,userId, message = "comment") == 0)
    }

    @Test
    fun test_errorCreateCommentById() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        try {
            noteService.createComment(noteId + 1, userId, message = "comment")
        } catch(e:RuntimeException) {
            assert(true)
        }
    }

    @Test(expected = RuntimeException::class)
    fun test_createComment_shouldThrowException() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test", canComment = false)
        noteService.createComment(noteId, userId, message = "comment")
    }

    @Test
    fun test_deleteComment() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        val commentId = noteService.createComment(noteId,userId, message = "comment")
        assertTrue(noteService.deleteComment(commentId, userId))
    }

    @Test
    fun test_editComment() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        val commentId = noteService.createComment(noteId,userId, message = "comment")
        assertTrue(noteService.editComment(commentId, userId, "update comment text"))
    }

    @Test
    fun test_restoreComment() {
        val userId = 1
        val noteService: NoteService = NoteService(userId, userId)
        val noteId = noteService.add("test", "test")
        val commentId = noteService.createComment(noteId,userId, message = "comment")
        noteService.deleteComment(noteId,userId)
        assertTrue(noteService.restoreComment(commentId, userId))
    }
}