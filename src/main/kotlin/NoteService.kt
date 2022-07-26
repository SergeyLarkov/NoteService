class NoteService(val myId: Int, var userId: Int)  {
    private val notes: MutableList<Note> = mutableListOf()
    private val comments: MutableList<Comment> = mutableListOf()

    fun add(title: String, text: String, privacy: Int = 0, canComment: Boolean = true): Int {
        notes.add(Note(
            if (notes.isEmpty()) 1 else notes.last().id+1,
            ownerId = userId,
            title = title,
            text = text,
            privacy = privacy,
            canComment = canComment))
        return if (notes.isEmpty()) 0 else notes.last().id
    }

    fun delete(noteId: Int): Boolean {
        try {
            val note = getById(noteId)
            if (note.ownerId != userId) {
                throw RuntimeException("It's not your note")
            }
            notes.remove(note)
            var i =  0
            while (i < comments.count() - 1) {
                if (comments[i].nid == noteId) {
                    comments.removeAt(i)
                } else {
                    i++
                }
            }
            return true
        } catch (e: RuntimeException) {
            println("Can't delete note, reason: "+e.message)
            return false
        }
    }

    fun edit(noteId: Int, title: String, text: String, privacy: Int = 0, canComment: Boolean = true): Boolean {
        try {
            val note = getById(noteId);
            if (note.ownerId != userId) {
                throw RuntimeException("It's not your note")
            }
            notes[notes.indexOf(note)] = note.copy(title = title, text = text, privacy = privacy, canComment = canComment)
            return true
        } catch (e: RuntimeException) {
            println("Can't edit note, reason: "+e.message)
            return false
        }
    }

    // Если count = 0 возвращаем все заметки
    fun get(userId:Int, offset:Int = 0, count:Int = 0, sort:Int = 0): List<Note> {
        val resultList: MutableList<Note> = mutableListOf()

        for (i in (offset .. notes.count() - 1)) {
            if (notes[i].ownerId == userId) {
                resultList.add(notes[i].copy())
            }
            if ((count != 0) && (resultList.count() == count)) {
                break
            }
        }
        when (sort) {
            0 -> return resultList.sortedBy {it.date}
            else -> return resultList.sortedByDescending {it.date}
        }
    }

    fun getById(id:Int): Note {
        for (note in notes) {
            if (note.id == id) {
                return note
            }
        }
        throw RuntimeException("Note with id $id not found")
    }

    // Если count = 0 возвращаем все заметки
    fun getFriendsNotes(offset:Int = 0, count:Int = 0): List<Note> {
        val friendsNotes: MutableList<Note> = mutableListOf()

        for (i in (offset .. notes.count() - 1)) {
            if (notes[i].ownerId != myId) {
                friendsNotes.add(notes[i].copy())
            }
            if ((count != 0) && (friendsNotes.count() == count)) {
                break
            }
        }
        return friendsNotes
    }

    fun createComment(noteId: Int, ownerId: Int, replyTo:Int=0, message:String): Int {
        val note = getById(noteId)
        if (note.canComment) {
            comments.add(Comment(
                if (comments.isEmpty()) 1 else comments.last().id+1,
                uid = ownerId,
                nid = note.id,
                oid = note.ownerId,
                message = message,
                replyTo = replyTo))
            note.comments++
            return if (comments.isEmpty()) 0 else comments.last().id
        } else {
            throw RuntimeException("Note is not allowed to comment")
        }
    }

    fun deleteComment(commentId: Int, ownerId: Int): Boolean {
        for ((index, comment) in comments.withIndex()) {
            if (comment.id == commentId) {
                if (comment.uid == ownerId) {
                    if (!comment.deleted) {
                        comments[index] = comment.copy(deleted = true)
                        val note = getById(comment.nid)
                        note.comments--
                        return true
                    }
                } else {
                    throw RuntimeException("It's not your comment")
                }
            }
        }
        return false
    }

    fun editComment(commentId: Int, ownerId: Int, message:String): Boolean {
        for ((index, comment) in comments.withIndex()) {
            if (comment.id == commentId) {
                if (comment.uid == ownerId) {
                    comments[index] = comment.copy(message = message)
                    return true
                } else {
                    throw RuntimeException("It's not your comment")
                }
            }
        }
        return false
    }

    // Если count = 0 возвращаем все комменты
    fun getComments(noteId: Int, ownerId: Int, sort: Int = 0, offset:Int = 0, count:Int = 0): List<Comment> {
        val resultList: MutableList<Comment> = mutableListOf()

        for (i in (offset .. comments.count() - 1)) {
            if ((comments[i].nid == noteId) && (comments[i].uid == ownerId) && (!comments[i].deleted)) {
                resultList.add(comments[i].copy())
            }
            if ((count != 0) && (resultList.count() == count)) {
                break
            }
        }
        when (sort) {
            0 -> return resultList.sortedBy {it.date}
            else -> return resultList.sortedByDescending {it.date}
        }
    }

    fun restoreComment(commentId: Int, ownerId: Int): Boolean {
        for ((index, comment) in comments.withIndex()) {
            if (comment.id == commentId) {
                if (comment.uid == ownerId) {
                    if (comment.deleted) {
                        comments[index] = comment.copy(deleted = false)
                        val note = getById(comment.nid)
                        note.comments++
                        return true
                    }
                } else {
                    throw RuntimeException("It's not your comment")
                }
            }
        }
        return false
    }
}