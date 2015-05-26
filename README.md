Provides a simple Java API for consuming the SNOMED CT terminology.  You can create a SQLite database from the TRUD CSV files,
provided by NHS Connecting for Health, available from https://www.uktcregistration.nss.cfh.nhs.uk/ (RFI release) using the loader package
and then use the db package to access it using the API defined in the root package. 

Note that the API is a "close to the metal" version - not very object orientated - mostly just a wrapper for the published csv files.  
It does not have any facility for creating pre- or post-processed queries for instance.

## Creating a SQLite database from a TRUD download.

Set up a copy of the project in your favourite IDE - I use eclipse

 * Copy etc/meta_core.txt.dist, rename it etc/meta_core.txt and edit it to point at your copy of the NHS trud SNOMED download.
 * Run loader.SnomedCoreLoader. This will create you a SQLite database, etc/snomed.sqlite that can back the DB implementation of the API.  
 * Run the ExampleHierarchy test

Tested with 20100731, 20110131, 20150131 release versions

## Using the API

The ExampleTopLevel test shows a simple example of using the API 

## Notes

### Database design

The database has five tables.  The Concept, Relationship and Description tables mirror the CSV files used to populate the database.
The Concept.FullySpecifiedName column has structured data embedded within it (i.e. "Gamma ray therapy (procedure)" is a "procedure")
that is extracted during loading and stored using the Suffix and ConceptSuffix cross table for easy access.

### API design

The API provides a lightweight wrapper around the database tables, which in turn inherit their design from the source CSV files.
This means that lots of Integers are passed around as proxies for concepts, relations, descriptions that must be inflated via the
central Snomed "datasource" instance.  

One confusing element is that sometimes primitive datatypes are used and sometimes their object equivalent are used.
This is due to the design choice of using java.util.Set as the container in the datasource object which requires the
object equivalent balanced with wanting to create a small footprint wrapper objects for Concept, Relationship and Description.
Also occasionally longs are used instead of ints.  longs are used for relationship IDs, and ints for everything else.  

An alternative design would access related Concept, Description and RelationShip directly, not via the datasource object. 
It would also be nice to access related concepts directly, instead of first inflating a Realtionship object.
Neither of these options have been implemented for simplicity sake.

## TODO
 
 - Wire suffixes into the API
 - An alternative DB implementation, e.g. MySQL
 - An alternative source, e.g. bioportal
 - A report that summarises the contents of the terminology
 - Test coverage for the Sqlite implementation of the API

## Related projects

 - bioportal (http://bioportal.bioontology.org/) provides a very good online web interface that covers many terminologies including snomed ct.
 - Snofyre (http://code.google.com/p/snofyre/) is a project from the NHS that looks a lot more developed than this one.



