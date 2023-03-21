const { createApp } = Vue

createApp({
    data() {
        return {
            data: [],
            accounts: [],
            transferOption: "",
            cardErrorCreation: "",
            amount: "",
            description: "",
            numberEmission: "",
            numberReceptor: "",
            nuevaLista: [],
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.data = response
                    this.accounts = response.data.accounts
                    console.log(this.accounts)
                })
        },
        transferencias() {
            axios.post('/api/transactions', `amount=${this.amount}&description=${this.description}&numberEmission=${this.numberEmission}&numberReceptor=${this.numberReceptor}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(response => {
                Swal.fire({
                    position: 'midle',
                    icon: 'success',
                    title: 'Your login have been successful',
                    background: '#000000c1',
                    color: 'var(--bs-highlight-bg)',
                    showConfirmButton: false,
                    timer: 2000
                })
                    .then(() => {
                        window.location.href = '/web/transfer.html';
                    })
            })
                .catch(error => {
                    this.cardErrorCreation = error.response.data
                    console.log(this.cardErrorCreation);
                    Swal.fire({
                        title: `${this.cardErrorCreation.error}` + " " + `(error: ${this.cardErrorCreation.status})`,
                        text: "¡Correct your data and try again!",
                        icon: "error",
                        dangerMode: true,
                    })
                })
        },
        changed() {
            this.nuevaLista = this.accounts.filter(account => account.number !== this.numberEmission)
            return this.nuevaLista
        },
        parseDate(date) {
            return date.split("T")[0]
        },
        soloAños(data) {
            let dia = data.split("-")[1]
            let años = data.split("-")[0]
            return años.slice(2, 4) + "-" + dia
        },
        alertLogOut() {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                background: '#000000c1',
                color: 'var(--bs-highlight-bg)',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.logout()
                }
            })
        },
        logout() {
            axios.post(`http://localhost:8080/api/logout`)
                .then(response => {
                    window.location.href = '/web/index.html';
                })
        },
    }
}).mount("#app")