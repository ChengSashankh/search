package fyi.deeno.protocols.data

case class Document(id: Long, title: String, text: String)
// TODO: EncodedDocument should be List[Long]
case class EncodedDocument(id: Long, text: String)

case class PositionalPosting(id: Long, pos: Long)
// TODO: Changing positional index to using PositionalPosting instead of List[Long]
// causes dataset typing to fail

// TODO: posIdx should be List[List[Long]]
case class PositionalIndex(id: Long, posIdx: String)

case class NewPositionalIndex(docId: Long, id: Long, indexes: List[Long])

case class Posting(word: String, id: Long, count: Long)
case class SubPosting(id: Long, count: Long)

case class TermFrequency(id: Long, freq: Long)
case class TfInvertedPosting(word: String, postings: List[TermFrequency])
case class InvertedPosting(word: String, docs: String)