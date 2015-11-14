package nbmtools

import java.util.zip.ZipFile

object NbmUtils {
    private implicit def toBiFunction[A, B, C](f: Function2[A, B, C]) = {
        new java.util.function.BiFunction[A, B, C] {
            override def apply(a: A, b: B): C = f(a, b)
        }
    }

    implicit def toZipEntryList(file: ZipFile): List[ZipEntry] = {
        def f(entry: ZipUtils.Entry, list: List[ZipEntry]): List[ZipEntry] = {
            ZipEntry.of(entry)::list
        }
        ZipUtils.fold(f _, Nil, file).reverse
    }
}
