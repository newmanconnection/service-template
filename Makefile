# import config.
# You can change the default config with `make cnf="config_special.env" build`
cnf ?= config.env
include $(cnf)
export $(shell sed 's/=.*//' $(cnf))

# import deploy config
# You can change the default deploy config with `make cnf="deploy_special.env" release`
dpl ?= deploy.env
include $(dpl)
export $(shell sed 's/=.*//' $(dpl))

# grep the version from the mix file
VERSION=$(shell cat version.txt)

# HELP
# This will output the help for each task
# thanks to https://marmelab.com/blog/2016/02/29/auto-documented-makefile.html
.PHONY: help build

help: ## This help.
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)

.DEFAULT_GOAL := help


# DOCKER TASKS
# Build the container
build: bump-version ## Build the container
	docker build -t newmanconnection/$(APP_NAME) .
	docker tag newmanconnection/$(APP_NAME) newmanconnection/$(APP_NAME):$(VERSION) 

build-nc: ## Build the container without caching
	docker build --no-cache -t newmanconnection/$(APP_NAME) .
	docker tag newmanconnection/$(APP_NAME) newmanconnection/$(APP_NAME):$(VERSION) 

run: ## Run container on port configured in `config.env`
	kubectl create -f kube.yml

update:
	kubectl set image deployment/$(APP_NAME) $(APP_NAME)=newmanconnection/$(APP_NAME):$(VERSION)

stop: ## Stop and remove a running container
	kubectl delete -f kube.yml

release: bump-patch-version build-nc publish 

minor-release: bump-minor-version build-nc publish 

major-release: bump-major-version build-nc publish 

bump-major-version:
	./inc-version.sh -M 

bump-minor-version:
	./inc-version.sh -m 

bump-patch-version:
	./inc-version.sh -p 

bump-version:
	./inc-version.sh

# Docker publish
publish: 
	@echo 'publish $(VERSION) to $(DOCKER_REPO)'
	docker tag newmanconnection/$(APP_NAME) $(DOCKER_REPO)/newmanconnection/$(APP_NAME):$(VERSION)
	docker tag newmanconnection/$(APP_NAME) $(DOCKER_REPO)/newmanconnection/$(APP_NAME):latest
	docker push $(DOCKER_REPO)/newmanconnection/$(APP_NAME):latest
	docker push $(DOCKER_REPO)/newmanconnection/$(APP_NAME):$(VERSION)

logs: ## Get logs from running container
	kubectl logs $(shell kubectl get pods | grep $(APP_NAME) | grep Running | cut -d ' ' -f 1) -f

forward: ## Get logs from running container
	kubectl port-forward $(shell kubectl get pods | grep $(APP_NAME) | grep Running | cut -d ' ' -f 1) 8080

prompt: ## Get logs from running container
	kubectl exec -it $(shell kubectl get pods | grep $(APP_NAME) | grep Running | cut -d ' ' -f 1) -- /bin/bash

# HELPERS
version: ## Output the current version
	@echo $(VERSION)
