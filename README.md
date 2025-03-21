# elastic-search-integrations

**Elastic Search Integrations** is a project that enables advanced data search and integration using **Elasticsearch**. The project includes the implementation and testing of various search functionalities based on user data such as **username**, **email**, and **location**.

Additionally, the **Elasticsearch index** is seamlessly integrated with a **PostgreSQL database**. Each time a specific entity is saved in the database, it is automatically indexed in Elasticsearch. This integration ensures that the data is easily searchable through Elasticsearch, allowing efficient queries to be performed on both the database and the search index simultaneously. This real-time synchronization of data between PostgreSQL and Elasticsearch provides enhanced search capabilities and scalability.

## Implemented Functionalities:

1. **Saving User to Database and Index**:
   - Users are saved both in the **PostgreSQL database** and the **Elasticsearch index**. Whenever a user is created or updated, the corresponding data is automatically indexed in Elasticsearch to enable fast and efficient searching.

2. **Search by Location**:
   - The project allows searching users based on their **location**.

3. **Search by Username**:
   - Users can be searched by their **username**, providing a fast way to look up users based on this unique identifier.

4. **Search by Location and Username**:
   - A combination of **location** and **username** search is available. This allows more refined queries, enabling users to be found by both their username and their location in a single query.

5. **Location Suggestion Functionality**:
   - A feature for **location suggestions** based on a prefix input is implemented. When a user provides a location prefix (e.g., the first few characters of a location), the system returns a list of matching locations. This functionality can be useful for autocomplete or auto-suggest features in search forms.

6. **Re-indexing Users**:
   - A **re-indexing mechanism** is included for the case when a batch of users is inserted into the PostgreSQL database via SQL. Once new users are added, the project can trigger a **Docker container** to automatically fetch all users from the database and re-index them in Elasticsearch. This ensures that the index is always up-to-date with the latest data from the database.
