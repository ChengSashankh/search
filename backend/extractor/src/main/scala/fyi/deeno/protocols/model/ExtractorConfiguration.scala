package fyi.deeno.protocols.model

case class ExtractorConfiguration(sourceDataPath: String, outputBasePath: String,
                                  redisHost: String, redisPort: Int)
