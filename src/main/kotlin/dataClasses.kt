// поля класса взяты из описания объекта заметка https://dev.vk.com/reference/objects/note
data class Note (
    val id: Int = 0,
    val ownerId: Int = 0,
    val title: String = "",
    val text: String,
    val date: Int = 0,
    var comments: Int = 0,
    var readComments: Int = 0,
    val viewUrl: String = "",
    // свойство privacy_view: String изменено на privacy: Int
    val privacy: Int = 0,
    val canComment: Boolean = true,
    val textWiki: String = ""
)

// поля класса взяты из описания возвращаемых объектов ф-ции getComments https://dev.vk.com/method/notes.getComments
data class Comment (
    val id: Int = 0,
    val uid: Int = 0,
    val nid: Int = 0,
    val oid: Int = 0,
    val date: Int = 0,
    val message: String = "",
    val replyTo: Int = 0,
    // дорбавлен признак удаления комментария
    var deleted: Boolean = false
)
