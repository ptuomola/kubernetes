# Exercise 3.06: DBaaS vs DIY

Pros of DBaaS (and conversely, cons of DIY):

- Automatic patching
- Automatic backups
- Faster to set-up
- Less effort to maintain
- Security set-up "out of the box" (encryption of data / secure connections) 
- Easier integration with Cloud Run / Cloud Functions
- Automatic storage scaling
- Easy to set up high availability / replication
- Easier user interface

Pros of DIY (and conversely, cons of DBaaS)

- Full control of configuration / settings / version
- All database features supported
- Cheaper in terms of cost to operate (though not time taken to set-up / maintain)

# Exercise 3.07: Commitment

Until now I've been using Postgres iwth PersistentVolumeClaims. However, I'll now set up in exercise 3.07, Google Cloud SQL and will use that for the remainder of the exercises. The reason for this is simply to get experience of both. 
