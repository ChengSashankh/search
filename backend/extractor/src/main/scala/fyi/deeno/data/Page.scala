package fyi.deeno.data

case class Page(id: Long, title: String, text: String)
case class Posting(word: String, id: Long, count: Long)
case class SubPosting(id: Long, count: Long)

case class TermFrequency(id: Long, freq: Long)
case class TfInvertedPosting(word: String, postings: List[TermFrequency])
case class InvertedPosting(word: String, docs: String)