1. Build and install controller and manifests:

cd controller
make
make install
make docker-build docker-push IMG=<repository>/<image>:<tag>
make deploy IMG=<repository>/<image>:<tag>

2. Apply manifest

cd ..
kubectl -f manifests

TODO: 
- Currently only one dummysite per cluster is supported - should enhance so that a single ingress can route to multiple dummysites
- Not all states can be successfully reconciled towards target (i.e. if deployment exists but service/ingress is missing)

