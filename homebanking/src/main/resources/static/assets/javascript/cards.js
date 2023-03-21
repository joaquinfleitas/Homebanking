const { createApp } = Vue

createApp({
    data() {
        return {
            data: [],
            cards: [],
            cardCredit: "",
            cardDebit: "",
            fechaDadaVuelta: "",
            number: "",
        }
    },
    created() {
        this.loadData()
        this.TarjetaVencida()
    },
    methods: {
        nuevaTarjeta() {
            axios.post('/api/clients/current/cards')
                .then(response => {
                })
                .catch(error => { console.log("hola"); })
        },
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.data = response
                    this.cards = response.data.cards
                    console.log(this.cards)
                    this.cardCredit = this.cards.filter(cards => cards.cardType == "CREDIT")
                    this.cardDebit = this.cards.filter(cards => cards.cardType == "DEBIT")
                })
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
            console.log("asd")
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
                    this.ocultarTarjeta()
                }
            })
        },
        TarjetaVencida() {
            let vencida = new Date();
            let opciones = { year: "numeric", month: "2-digit", day: "2-digit" };
            let fechaFormateada = vencida.toLocaleString("es-ES", opciones);
            this.fechaDadaVuelta = fechaFormateada.split("/").reverse().join("-")
            console.log(this.fechaDadaVuelta)
        },
        ocultarTarjeta() {
            console.log(this.number);
            axios.patch("/api/clients/current/cards", `number=${this.number}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(response => {
                window.location.href = '/web/cards.html'
            })
        },
    }
}).mount("#app")
