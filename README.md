# kopama

Kopama ("Kotlin Pattern Matching") provides pattern matching functionality, as known from Haskell and Scala. It not only supports built-in classes, but also custom data classes (although this requires the use of KSP)

Unfortunately the project had to be completely reworked, in order to make it more type-safe. The main reason for the carnage is the use of annotation processing (specifically KSP), which requires a multi-project setup. I'm terribly sorry I had to do this, but I see a chance to go from a proof-of-concept toy to a stable and actually useful implementation.

The README will be updated as soon as the new version stabilizes.