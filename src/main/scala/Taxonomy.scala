/**
  * Created by rob on 23/06/16.
  */

import scalaz._
import Scalaz._
import scala.annotation.tailrec
import scalaz.Tree.{Leaf, Node}

sealed trait LanguageId
case object En_GB extends LanguageId
case object Fr_FR extends LanguageId
case object It_IT extends LanguageId

case class CategoryId(name: String)
case class TagName(name: String)

case class Taxonomy(tree: Tree[CategoryId],
                    translations: Map[TagName, Map[LanguageId,String]] = Map(),
                    tags: Map[TagName, Set[CategoryId]] = Map()) {

  def addTranslation(tagName: TagName, languageId: LanguageId, string: String) =
    this.copy( translations = translations |+| Map(tagName -> Map(languageId -> string)))

  def linkTag( tagName: TagName, id: CategoryId ) =
    this.copy( tags = tags |+| Map(tagName -> Set(id) ))

  def retrieveNodeById(categoryId: CategoryId) =
    Taxonomy.getDescendants(Set(categoryId), Stream.empty, Stream(tree))

  def retrieveAllNodesWithTag(tagName: TagName) =
    tags.get(tagName).toList.toStream.flatMap( Taxonomy.getDescendants(_, Stream.empty, Stream(tree)) )
}

object Taxonomy {

  /**
    * done so if the same category id is repeated inside a subtree we will match at all levels
    *
    * there might be a more clever way to do this with scalaz traversable
    */
  @tailrec
  def getDescendants(ids: Set[CategoryId], matchedTrees: Stream[Tree[CategoryId]], trees: Stream[Tree[CategoryId]]): Stream[Tree[CategoryId]]  = {
    val (matched, notMatched) = trees.partition( t => ids.contains(t.rootLabel) )
    val newMatched = matchedTrees ++ matched
    val toTry = notMatched.map( _.subForest ).filterNot( _.isEmpty ).flatten
    if (toTry.isEmpty)
      newMatched
    else
      getDescendants( ids, newMatched, toTry)
  }

  implicit def IdShow = Show.shows( (id: CategoryId) => id.toString )
}
