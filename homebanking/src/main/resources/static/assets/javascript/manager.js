const { createApp } = Vue

createApp({
    data() {
        return {
            data: undefined,
            clients: [],
            nuevosClientes: { firstName: "", lastName: "", email: "" },
            selectClient: { firstName: "", lastName: "", email: "" },
        }

    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients")
                .then(response => {
                    this.data = response
                    console.log(response)
                    this.clients = this.data.data
                })
                .catch(error => console.log(error))
        },
        addClient() {
            if (this.nuevosClientes.firstName && this.nuevosClientes.lastName && this.nuevosClientes.email) {
                axios.post("http://localhost:8080/api/clients", this.nuevosClientes)
                    .then(response => {
                        this.loadData()
                        this.refreshForm()
                    })
            } else {
                alert("missing fields to fill")
            }

        },
        editClient() {
            axios.put(`http://localhost:8080/api/clients`, this.selectClient)
            this.selectClient = this.nuevosClientes
                .then(response => {
                    this.loadData();
                    this.refreshForm();
                    this.editMode(false);
                })
        },
        startEdit(client) {
            this.nuevosClientes.firstName = client.firstName;
            this.nuevosClientes.lastName = client.lastName;
            this.nuevosClientes.email = client.email;
            this.nuevosClientes.id = client._links.client.href;
            this.editMode = true;
        },
        refreshForm() {
            this.nuevosClientes = { firstName: "", lastName: "", email: "" };
        },
        deleteClient(client) {
            axios.delete(client._links.client.href)
                .then(response => {
                    this.loadData()
                })
        },
    }

}).mount('#app')