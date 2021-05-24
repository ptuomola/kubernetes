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

I will keep using Postgres with PersistentVolumeClaims, as I have already gone through the trouble of setting that up - and hence can now benefit from lower costs without additional effort. Also the other benefits of a managed database do not really apply to these exercises, as we will not be storing any data that would be confidential / need to be backed up etc. 
