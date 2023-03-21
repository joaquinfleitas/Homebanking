const { createApp } = Vue

createApp({
    data() {
        return {
            data: [],
            cardErrorCreation: "",
            accounts: [],
            accountLoan: "",
            amountLoan: "",
            loanOption: "",
            accountPayment: "",
            loans: [],
        }
    },
    created() {
        this.loadData()
        this.loan()
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.data = response
                    this.accounts = response.data.accounts;
                    console.log(this.loans)
                })
        },
        loan() {
            axios.get("http://localhost:8080/api/loans")
                .then((response) => {
                    this.loans = response.data
                    this.fees = response.data[0].fee
                    console.log(this.fees)
                })
        },
        paymentFilter() {
            return this.loans.filter(loan => loan.id == this.loanOption)[0]?.payment;
        },
        alertCrear() {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, i need it!!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.createLoan()
                }
            })
        },
        createLoan() {
            axios.post('/api/loans', { id: this.loanOption, amount: this.amountLoan, payments: this.accountPayment, numberAccountDestiny: this.accountLoan }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                Swal.fire({
                    position: 'midle',
                    icon: 'success',
                    title: 'Your loan has been created successful',
                    background: '#000000c1',
                    color: 'var(--bs-highlight-bg)',
                    showConfirmButton: false,
                    timer: 1500
                })
                    .then(() => {
                        window.location.href = '/web/loan-application.html';
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


/* pagosPorMes() {
    let amountMultiplicacion = (this.amountLoan * this.fees) / this.accountPayment
    console.log(this.fees)
    return amountMultiplicacion.toLocaleString('de-DE', { style: 'currency', currency: 'USD' })
}, */