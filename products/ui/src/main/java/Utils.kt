import ru.kode.base.internship.products.domain.entity.Money


fun Money.format(): String {
  return "$amount ${sign.code}"
}