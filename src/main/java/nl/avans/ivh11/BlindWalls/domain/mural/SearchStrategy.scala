package nl.avans.ivh11.BlindWalls.domain.mural

import java.lang
import java.util
import scala.collection.JavaConverters._

/**
  * Created by Noureddine on 3-4-2018
  */

trait SearchStrategy {
  def searchMural(searchString: String, murals: java.lang.Iterable[Mural]) : java.lang.Iterable[Mural]
}

class NameStrategy extends SearchStrategy {

  override def searchMural(searchString: String, murals: lang.Iterable[Mural]): lang.Iterable[Mural] = {
    val searchedMurals : util.ArrayList[Mural] = new util.ArrayList[Mural]()
    for (mural: Mural <- murals.asScala) {
      val muralName = mural.asInstanceOf[Mural].getClass.getName.toLowerCase
      val searchedMural = muralName match {
        case `muralName` if muralName.contains(searchString.toLowerCase) => mural
        case _ => null
      }
      if (searchedMural != null) {
        searchedMurals.add(mural);
      }
    }
    searchedMurals;
  }
}