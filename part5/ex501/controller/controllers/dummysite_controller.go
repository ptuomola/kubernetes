/*
Copyright 2021.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package controllers

import (
	"context"
	"fmt"

	"github.com/go-logr/logr"
	kapps "k8s.io/api/apps/v1"
	kcore "k8s.io/api/core/v1"
	knetworking "k8s.io/api/networking/v1beta1"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/util/intstr"
	ctrl "sigs.k8s.io/controller-runtime"
	"sigs.k8s.io/controller-runtime/pkg/client"

	stabledwkv1 "stable.dwk/api/v1"
)

// DummySiteReconciler reconciles a DummySite object
type DummySiteReconciler struct {
	client.Client
	Log    logr.Logger
	Scheme *runtime.Scheme
}

//+kubebuilder:rbac:groups=stable.dwk.stable.dwk,resources=dummysites,verbs=get;list;watch;create;update;patch;delete
//+kubebuilder:rbac:groups=stable.dwk.stable.dwk,resources=dummysites/status,verbs=get;update;patch
//+kubebuilder:rbac:groups=stable.dwk.stable.dwk,resources=dummysites/finalizers,verbs=update
//+kubebuilder:rbac:groups=apps,resources=deployments,verbs=get;list;watch;create;update;patch;delete
//+kubebuilder:rbac:groups=core,resources=services,verbs=get;list;watch;create;update;patch;delete
//+kubebuilder:rbac:groups=networking.k8s.io,resources=ingresses,verbs=get;list;watch;create;update;patch;delete

// Reconcile is part of the main kubernetes reconciliation loop which aims to
// move the current state of the cluster closer to the desired state.
//
// For more details, check Reconcile and its Result here:
// - https://pkg.go.dev/sigs.k8s.io/controller-runtime@v0.7.2/pkg/reconcile
func (r *DummySiteReconciler) Reconcile(ctx context.Context, req ctrl.Request) (ctrl.Result, error) {
	log := r.Log.WithValues("dummysite", req.NamespacedName)

	var dummySite stabledwkv1.DummySite

	if err := r.Get(ctx, req.NamespacedName, &dummySite); err != nil {
		log.Error(err, "unable to fetch DummySite")
		return ctrl.Result{}, client.IgnoreNotFound(err)
	}

	selector := make(map[string]string)
	podName := fmt.Sprintf("%s-pod", dummySite.Name)
	selector["app"] = podName
	svcName := fmt.Sprintf("%s-svc", dummySite.Name)

	constructIngressForDummySite := func(dummySite *stabledwkv1.DummySite) (*knetworking.Ingress, error) {
		ingressName := fmt.Sprintf("%s-ing", dummySite.Name)
		pathType := knetworking.PathTypePrefix

		service := &knetworking.Ingress{
			ObjectMeta: metav1.ObjectMeta{
				Labels:      make(map[string]string),
				Annotations: make(map[string]string),
				Name:        ingressName,
				Namespace:   dummySite.Namespace,
			},
			Spec: knetworking.IngressSpec{
				Rules: []knetworking.IngressRule{
					{
						IngressRuleValue: knetworking.IngressRuleValue{
							HTTP: &knetworking.HTTPIngressRuleValue{
								Paths: []knetworking.HTTPIngressPath{
									{
										Path:     "/",
										PathType: &pathType,
										Backend: knetworking.IngressBackend{
											ServiceName: svcName,
											ServicePort: intstr.FromInt(2347),
										},
									},
								},
							},
						},
					},
				},
			},
		}

		log.V(1).Info("variables for service", "service", service)

		if err := ctrl.SetControllerReference(dummySite, service, r.Scheme); err != nil {
			return nil, err
		}

		return service, nil
	}

	constructServiceForDummySite := func(dummySite *stabledwkv1.DummySite) (*kcore.Service, error) {

		service := &kcore.Service{
			ObjectMeta: metav1.ObjectMeta{
				Labels:      make(map[string]string),
				Annotations: make(map[string]string),
				Name:        svcName,
				Namespace:   dummySite.Namespace,
			},
			Spec: kcore.ServiceSpec{
				Type:     kcore.ServiceTypeClusterIP,
				Selector: selector,
				Ports: []kcore.ServicePort{
					{
						Port:       2347,
						Protocol:   kcore.ProtocolTCP,
						TargetPort: intstr.FromInt(8080),
					},
				},
			},
		}

		log.V(1).Info("variables for service", "service", service)

		if err := ctrl.SetControllerReference(dummySite, service, r.Scheme); err != nil {
			return nil, err
		}

		return service, nil
	}

	constructDeploymentForDummySite := func(dummySite *stabledwkv1.DummySite) (*kapps.Deployment, error) {
		depName := fmt.Sprintf("%s-dep", dummySite.Name)

		numReplicas := int32(1)

		deployment := &kapps.Deployment{
			ObjectMeta: metav1.ObjectMeta{
				Labels:      make(map[string]string),
				Annotations: make(map[string]string),
				Name:        depName,
				Namespace:   dummySite.Namespace,
			},
			Spec: kapps.DeploymentSpec{
				Replicas: &numReplicas,
				Selector: &metav1.LabelSelector{
					MatchLabels: selector,
				},
				Template: kcore.PodTemplateSpec{
					ObjectMeta: metav1.ObjectMeta{
						Labels:      selector,
						Annotations: make(map[string]string),
						Name:        podName,
						Namespace:   dummySite.Namespace,
					},
					Spec: kcore.PodSpec{
						Containers: []kcore.Container{
							{
								Name:            podName,
								Image:           "ptuomola/dummysite",
								ImagePullPolicy: "Always",
								Env: []kcore.EnvVar{
									{
										Name:  "WEBSITE_URL",
										Value: dummySite.Spec.WebsiteUrl,
									},
								},
							},
						},
					},
				},
			},
		}

		log.V(1).Info("variables for deployment", "deployment", deployment)

		if err := ctrl.SetControllerReference(dummySite, deployment, r.Scheme); err != nil {
			return nil, err
		}

		return deployment, nil
	}

	deployment, err := constructDeploymentForDummySite(&dummySite)

	if err != nil {
		log.Error(err, "unable to construct deployment for dummysite")
		return ctrl.Result{}, nil
	}

	if err := r.Create(ctx, deployment); err != nil {
		log.Error(err, "unable to create deployment for DummySite", "deployment", deployment)
		return ctrl.Result{}, err
	}

	log.V(1).Info("created Deployment for DummySite", "deployment", deployment)

	service, err := constructServiceForDummySite(&dummySite)

	if err != nil {
		log.Error(err, "unable to construct service for dummysite")
		return ctrl.Result{}, nil
	}

	if err := r.Create(ctx, service); err != nil {
		log.Error(err, "unable to create service for DummySite", "service", service)
		return ctrl.Result{}, err
	}

	log.V(1).Info("created Service for DummySite", "service", service)

	ingress, err := constructIngressForDummySite(&dummySite)

	if err != nil {
		log.Error(err, "unable to construct ingress for dummysite")
		return ctrl.Result{}, nil
	}

	if err := r.Create(ctx, ingress); err != nil {
		log.Error(err, "unable to create ingress for DummySite", "ingress", ingress)
		return ctrl.Result{}, err
	}

	log.V(1).Info("created Ingress for DummySite", "ingress", ingress)

	return ctrl.Result{}, nil
}

// SetupWithManager sets up the controller with the Manager.
func (r *DummySiteReconciler) SetupWithManager(mgr ctrl.Manager) error {
	return ctrl.NewControllerManagedBy(mgr).
		For(&stabledwkv1.DummySite{}).
		Complete(r)
}
