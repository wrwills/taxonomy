import org.scalatest.{FlatSpec, Matchers, ShouldMatchers}

import scalaz._
import Scalaz._
import scalaz.Tree.{Leaf, Node}

class TaxonomySpecs extends FlatSpec with Matchers {
  import Taxonomy._

  val taxonomy = {
    val tree =
      CategoryId("categories").node(
        CategoryId("shows").node(
          CategoryId("theatre").leaf, CategoryId("films").node(
            CategoryId("chinese").leaf, CategoryId("comedy").leaf, CategoryId("action").leaf )),
        CategoryId("music").node(
          CategoryId("jazz").leaf, CategoryId("pop").leaf, CategoryId("rock").leaf ),
        CategoryId("restaurants").node(
          CategoryId("chineser").leaf, CategoryId("french").leaf, CategoryId("italian").leaf )
      )

    Taxonomy(tree)
      .addTranslation( TagName("chinese"), En_GB, "Chinese")
      .addTranslation( TagName("chinese"), Fr_FR, "Chinois")
      .addTranslation( TagName("chinese"), It_IT, "Chinese")
      .addTranslation( TagName("chinese"), Fr_FR, "Restaurants")
      .linkTag( TagName("chinese"), CategoryId("chinese") )
      .linkTag( TagName("chinese"), CategoryId("chineser") )
      .linkTag( TagName("restaurant"), CategoryId("restaurants") )
  }

  println(taxonomy.tree.drawTree)

  "taxonomy" should "behave get all nodes matching a tag name" in {
    val rslt = taxonomy.retrieveAllNodesWithTag(TagName("chinese")).toList
    rslt.head.rootLabel shouldBe (CategoryId("chineser"))
    rslt(1).rootLabel shouldBe (CategoryId("chinese"))
  }

  "taxonomy" should "be able to retrieve a node" in {
    val rslt = taxonomy.retrieveNodeById(CategoryId("music")).head
    rslt.rootLabel shouldBe (CategoryId("music"))
    rslt.subForest.toList.size shouldBe (3)
  }

}
