Perfeito 👍
Aqui vai um **How-To básico de Kubernetes**, com explicação dos conceitos principais e uma **tabela com os comandos mais usados** e o que cada um faz.

---

## 🧭 O que é Kubernetes?

**Kubernetes (K8s)** é uma plataforma open source para **gerenciar, escalar e orquestrar containers** (como Docker containers) em clusters de servidores.
Ele automatiza a implantação, o balanceamento de carga, a recuperação de falhas e a escalabilidade das aplicações.

---

## 🧩 Conceitos fundamentais

| Conceito               | Descrição                                                           |
| ---------------------- | ------------------------------------------------------------------- |
| **Cluster**            | Conjunto de máquinas (nós) gerenciadas pelo Kubernetes.             |
| **Node (nó)**          | Máquina física ou virtual que executa os containers.                |
| **Pod**                | A menor unidade do Kubernetes, que encapsula um ou mais containers. |
| **Deployment**         | Controla a criação e atualização dos Pods.                          |
| **Service**            | Exposição estável (IP/porta) para acesso aos Pods.                  |
| **Namespace**          | Particiona recursos dentro do cluster.                              |
| **ConfigMap / Secret** | Armazena configurações e credenciais para uso nos Pods.             |

---

## 💻 Comandos básicos do Kubernetes

| Comando                                                               | Descrição                                                   |
| --------------------------------------------------------------------- | ----------------------------------------------------------- |
| `kubectl version`                                                     | Mostra a versão do cliente e do servidor Kubernetes.        |
| `kubectl cluster-info`                                                | Exibe informações sobre o cluster e os serviços principais. |
| `kubectl get nodes`                                                   | Lista todos os nós do cluster.                              |
| `kubectl get pods`                                                    | Lista todos os pods no namespace atual.                     |
| `kubectl get pods -A`                                                 | Lista todos os pods em todos os namespaces.                 |
| `kubectl get services`                                                | Lista os serviços criados no cluster.                       |
| `kubectl get deployments`                                             | Mostra os deployments ativos.                               |
| `kubectl describe pod <nome-do-pod>`                                  | Mostra detalhes e eventos de um pod específico.             |
| `kubectl logs <nome-do-pod>`                                          | Exibe os logs de um pod.                                    |
| `kubectl exec -it <nome-do-pod> -- /bin/sh`                           | Acessa o terminal dentro de um container em execução.       |
| `kubectl apply -f <arquivo.yaml>`                                     | Cria ou atualiza recursos definidos em um arquivo YAML.     |
| `kubectl create -f <arquivo.yaml>`                                    | Cria recursos a partir de um arquivo YAML.                  |
| `kubectl delete -f <arquivo.yaml>`                                    | Remove recursos definidos em um arquivo YAML.               |
| `kubectl delete pod <nome-do-pod>`                                    | Deleta um pod específico.                                   |
| `kubectl rollout status deployment/<nome>`                            | Mostra o status da atualização de um deployment.            |
| `kubectl rollout undo deployment/<nome>`                              | Reverte um deployment para a versão anterior.               |
| `kubectl scale deployment <nome> --replicas=<n>`                      | Ajusta a quantidade de réplicas de um deployment.           |
| `kubectl get namespaces`                                              | Lista todos os namespaces.                                  |
| `kubectl config get-contexts`                                         | Mostra os contextos de configuração (clusters acessíveis).  |
| `kubectl config use-context <contexto>`                               | Alterna entre clusters ou contextos.                        |
| `kubectl port-forward pod/<nome> <porta_local>:<porta_pod>`           | Encaminha uma porta local para o pod.                       |
| `kubectl expose deployment <nome> --type=LoadBalancer --port=<porta>` | Cria um service para expor o deployment.                    |
| `kubectl get events`                                                  | Lista eventos recentes no cluster (útil para debug).        |

---

## 🚀 Fluxo básico de uso

1. **Criar um arquivo YAML** (por exemplo `deployment.yaml`) descrevendo o app (Pod, Deployment, Service, etc.)
2. **Aplicar o arquivo**:

   ```bash
   kubectl apply -f deployment.yaml
   ```
3. **Verificar se tudo subiu corretamente**:

   ```bash
   kubectl get pods
   kubectl get services
   ```
4. **Ver logs ou acessar o container**:

   ```bash
   kubectl logs <pod>
   kubectl exec -it <pod> -- /bin/sh
   ```
5. **Remover quando não precisar mais**:

   ```bash
   kubectl delete -f deployment.yaml
   ```

---

Vamos destrinchar **`port`**, **`targetPort`** e **`nodePort`** de forma simples, visual e prática.

---

## 🎯 Contexto rápido

Seu **Pod** (onde roda o WildFly) expõe a porta **8080** internamente:

```yaml
containers:
  - name: garage474-main-service
    ports:
      - containerPort: 8080
```

Mas o Kubernetes **não deixa você acessar um Pod diretamente de fora** (por segurança e abstração).
Então você cria um **Service**, que serve como “ponte” entre o **mundo externo** e o **Pod**.

---

## ⚙️ O papel de cada porta

Vamos usar um exemplo prático:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: myapp
spec:
  type: NodePort
  selector:
    app: garage474-main-service
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
      nodePort: 30080
```

Agora, veja o fluxo:

```
[Usuário navegador]
       │
       ▼
http://<minikube-ip>:30080
       │
       ▼
Service (porta 80)
       │
       ▼
Pod (porta 8080)
```

---

## 🔍 Entendendo cada campo

| Campo             | Onde fica                           | O que faz                                              | Exemplo |
| ----------------- | ----------------------------------- | ------------------------------------------------------ | ------- |
| **containerPort** | no Pod                              | Porta **interna do container** onde a aplicação escuta | `8080`  |
| **targetPort**    | no Service                          | Porta **no Pod** para onde o tráfego será enviado      | `8080`  |
| **port**          | no Service                          | Porta **dentro do Service** (acessada por outros Pods) | `80`    |
| **nodePort**      | no Service (só se `type: NodePort`) | Porta **no nó/host** (acessada externamente)           | `30080` |

---

## 💡 Analogia simples

Imagine o **Service** como uma central telefônica:

| Nível                            | Representação              | Porta                   |
| -------------------------------- | -------------------------- | ----------------------- |
| **Mundo externo (host)**         | o número da central        | `nodePort` (ex: 30080)  |
| **Service (a central)**          | a linha interna da empresa | `port` (ex: 80)         |
| **Pod (o ramal do funcionário)** | o ramal final              | `targetPort` (ex: 8080) |

Quando alguém liga para `30080`, o Kubernetes redireciona para o Service (`80`), que conecta ao Pod (`8080`).

---

## 🧩 Quando usar cada um

| Situação                       | Campos usados                    | Explicação                                    |
| ------------------------------ | -------------------------------- | --------------------------------------------- |
| Comunicação interna entre Pods | `port`, `targetPort`             | Não precisa de `nodePort` nem Ingress         |
| Acesso externo (ex: navegador) | `port`, `targetPort`, `nodePort` | Usa `type: NodePort`                          |
| Domínio e HTTPS                | `port`, `targetPort` + Ingress   | Ingress faz o papel do “NodePort inteligente” |

---

## 🔎 Exemplo prático no seu caso

Seu WildFly escuta em **8080** → é o `targetPort` (porta no Pod).
Você escolheu `port: 80` → é a porta "pública" do Service dentro do cluster.
E com `nodePort: 30080` → você consegue acessar de fora do cluster (ex: do navegador).

Assim:

```
http://<minikube-ip>:30080  →  encaminha para 8080 no Pod
```

---

## ✅ Resumo final

| Campo           | Local   | Significado                                                    | Exemplo | Necessário para             |
| --------------- | ------- | -------------------------------------------------------------- | ------- | --------------------------- |
| `containerPort` | Pod     | Onde a app escuta                                              | 8080    | Sempre                      |
| `targetPort`    | Service | Porta do Pod onde o Service envia o tráfego                    | 8080    | Sempre                      |
| `port`          | Service | Porta exposta dentro do cluster (outros Pods acessam por aqui) | 80      | Sempre                      |
| `nodePort`      | Service | Porta exposta fora do cluster (para o host externo)            | 30080   | Só se quiser acesso externo |

---


