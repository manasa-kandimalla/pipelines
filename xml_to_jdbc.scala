import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.Almaren
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SaveMode
import com.github.music.of.the.ainur.quenya.QuenyaDSL

val almaren = Almaren("Custom code")

val sourceDF = almaren.builder.sourceFile("xml","s3a://gratis-bucket-test/mt4002_testing/catalog.xml",Map("rootTag"->"catalog","rowTag"->"product")).batch

val quenyaDsl = QuenyaDSL
//quenyaDsl.printDsl(sourceDF)

val dsl = quenyaDsl.compile("""_description$_description:StringType
_product_image$_product_image:StringType
catalog_item@catalog_item
        catalog_item._gender$_gender:StringType
        catalog_item.item_number$item_number:StringType
        catalog_item.price$price:DoubleType
        catalog_item.size@size
                size._description$_size_description:StringType
                size.color_swatch@color_swatch
                        color_swatch._VALUE$_VALUE:StringType
                        color_swatch._image$_image:StringType""".stripMargin)

val df_final= quenyaDsl.execute(dsl,sourceDF)
df_final.show(false)

almaren.builder.sourceDataFrame(df_final).targetJdbc("jdbc:postgresql://w3.training5.modak.com:5432/training","org.postgresql.Driver","tr5.mt4002_catalog_xml",SaveMode.Overwrite,Some("mt4002"),Some("mt4002@m02y22"),Map()).batch

