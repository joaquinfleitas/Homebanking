const { createApp } = Vue

createApp({
    data() {
        return {
            data: [],
            accounts: [],
            clients: [],
            loans: [],
            nombreDeCuenta: "",
            accountType: "",
        }
    },
    created() {
        this.loadData()
    },

    methods: {
        nuevaCuenta() {
            axios.post('/api/clients/current/accounts', `accountType=${this.accountType}`)
                .then(response => {
                    this.loadData();
                })
                .catch(error => { console.log(error); })
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
        loadData() {
            axios.get(`http://localhost:8080/api/clients/current`)
                .then(response => {
                    this.data = response
                    this.accounts = this.data.data.accounts
                    this.clients = response.data
                    this.loans = response.data.loans
                    console.log(this.loans)

                })
        },
        parseDate(date) {
            return date.split("T")[0]
        },
        alertOcultar() {
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
                    this.ocultarAccount()
                }
            })
        },
        ocultarAccount() {
            axios.patch("/api/clients/current/accounts", `name=${this.nombreDeCuenta}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(response => {
                window.location.href = '/web/accounts.html'
            })
        },
        alertCrearAccount() {
            Swal.fire({
                title: 'Choose your Account Type',
                background: '#000000c1',
                color: 'var(--bs-highlight-bg)',
                showDenyButton: true,
                showCancelButton: true,
                confirmButtonText: 'Current Account',
                denyButtonText: `Saving Account`,
            }).then((result) => {
                if (result.isConfirmed) {
                    this.accountType = 'CURRENT'
                    this.nuevaCuenta()
                } else if (result.isDenied) {
                    this.accountType = 'SAVING'
                    this.nuevaCuenta()
                }
            })
        },
    }
}).mount("#app")