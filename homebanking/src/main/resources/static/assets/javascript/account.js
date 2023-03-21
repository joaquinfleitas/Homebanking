const { createApp } = Vue

createApp({
    data() {
        return {
            data: null,
            transactions: [],
            clients: [],
            account: null,
        }
    },
    created() {
        this.loadData()
    },

    methods: {
        loadData() {
            let url = new URLSearchParams(location.search).get("id")
            axios.get(`http://localhost:8080/api/accounts/${url}`)
                .then(response => {
                    this.transactions = response.data.transaction
                    console.log(this.transactions);
                })
        },
        parseDate(date) {
            return date.split("T")[0]
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