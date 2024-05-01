import ru.kode.base.internship.products.domain.entity.Money


fun Money.format(): String {
  val pointIdx = maxOf(amount.indexOf('.'), amount.indexOf(','))
  val res = if (pointIdx != -1) {
    var h = amount.substring(0 until pointIdx)
    val l = amount.substring(pointIdx + 1).take(2).let { if (it.length < 2) "${it}0" else it }
    h = buildString {
      var c = 0
      for (i in h.length - 1 downTo 0) {
        if (c < 3) {
          append(h[i])
          c++
        } else {
          append(" ${h[i]}")
          c = 1
        }
      }
    }.reversed()
    "$h,$l"
  } else {
    buildString {
      var c = 0
      amount.reversed().forEach {
        if (c < 3) {
          append(it)
          c++
        } else {
          append(" $it")
          c = 1
        }
      }
    }.reversed() + ",00"
  }
  return "$res ${sign.code}"
}


