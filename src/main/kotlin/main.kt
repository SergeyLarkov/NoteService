import java.lang.RuntimeException

fun main() {
    val myId = 1; // типа мой идентификатор юзера.

    val noteService: NoteService = NoteService(myId, myId)

    noteService.add("первая заметка", "текст заметки № 1")
    noteService.add("вторая заметка", "текст заметки № 2")
    noteService.add("третья заметка", "текст заметки № 3")

    noteService.userId = 2 // типа идентификатор друга.
    noteService.add("четвертая заметка", "текст заметки № 4")
    noteService.add("пятая  заметка", "текст заметки № 5", canComment = false)

    noteService.userId = 3 // типа идентификатор друга.
    noteService.add("шестая заметка", "текст заметки № 6")
    noteService.add("седьмая заметка", "текст заметки № 7")

    println("Заметки пользователя $myId:\n " + noteService.get(myId).toString())
    println("Заметки пользователя 2:\n" + noteService.get(2).toString())
    println("Заметки друзей:\n" + noteService.getFriendsNotes().toString())

    noteService.userId = myId
    println("Получить заметку по id = 1:\n" + noteService.getById(1).toString())
    noteService.edit(1, "first note","text of note No 1")
    println("Получить заметку по id = 1:\n" + noteService.getById(1).toString())

    println("Попытка редактировать чужую заметку")
    noteService.edit(5, "fifth note","text of note No 5")

    println("Удаляем заметку")
    if (noteService.delete(2)) {
        println("Note with id = 2 deleted")
    }

    println("Попытка удалить несуществующую заметку")
    noteService.delete(15)

    println("Попытка удалить чужую заметку")
    noteService.delete(5)

    noteService.createComment(1, myId, message = "первый комментарий")
    noteService.createComment(1, myId, message = "второй  комментарий")
    noteService.createComment(3, 2, message = "третий  комментарий")
    noteService.createComment(4, 3, message = "четвертый комментарий")

    println("Комментарии к заметке №1:\n" + noteService.getComments(1, myId))

    println("поменяли комментарий")
    noteService.editComment(1, myId, "first comment")
    println("Комментарии к заметке №1:\n" + noteService.getComments(1, myId))

    println("удалили комментарий")
    noteService.deleteComment(1, myId)
    println("Комментарии к заметке №1:\n" + noteService.getComments(1, myId))

    println("восстановили комментарий")
    noteService.restoreComment(1, myId)
    println("Комментарии к заметке №1:\n" + noteService.getComments(1, myId))

    // попытка отредактировать чужой комментарий
    try {
        noteService.editComment(3, myId, "Comment No 3")
    } catch (e:RuntimeException) {
        println(e.message)
    }

    // попытка удалить чужой комментарий
    try {
        noteService.deleteComment(3, myId)
    } catch (e:RuntimeException) {
        println(e.message)
    }

    // попытка создать комментарий у заметки, где стоит запрет на комментарии
    try {
        noteService.createComment(5, myId, message = "пятый комментарий")
    } catch (e:RuntimeException) {
        println(e.message)
    }
}