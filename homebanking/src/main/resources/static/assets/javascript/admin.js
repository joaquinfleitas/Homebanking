const { createApp } = Vue

createApp({
    data() {
        return {
            data: [],
            cardErrorCreation: "",
            name: "",
            maxAmount: "",
            payments: "",
            fee: "",
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
                    this.cards = response.data.cards
                    console.log(this.cards)
                })
        },
        createLoan() {
            axios.post('/api/admin/loan', `name=${this.name}&maxAmount=${this.maxAmount}&payments=${this.payments}&fee=${this.fee}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(response => {
                Swal.fire({
                    position: 'midle',
                    icon: 'success',
                    title: 'Your Loan has been created successful',
                    background: '#000000c1',
                    color: 'var(--bs-highlight-bg)',
                    showConfirmButton: false,
                    timer: 2000
                })
                    .then(() => {
                        window.location.href = '/web/admin.html';
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