import {useState} from "react";

function EmployeeForm() {
    const [name, setName] = useState('')
    const [phone, setPhone] = useState('')
    const [email, setEmail] = useState('')
    const [error, setError] = useState<string | null>(null)
    const [success, setSuccess] = useState(false)

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setError(null)
        setSuccess(false)

        try {
            const response = await fetch("http://localhost:8080/employee", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({name, phone, email}),
            })

            if (!response.ok) {
                throw new Error(response.statusText)
            }

            setSuccess(true)
            setName("")
            setPhone("")
            setEmail("")
        } catch (err) {
            setError("Nu s-a salvat")
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            {error && <p>{error}</p>}
            {success && <p>Angajat salvat</p>}
            <input
                type="text"
                placeholder="Employee Name"
                name="employeeName"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />
            <input
                type="text"
                placeholder="Phone Number"
                name="phoneNumber"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
            />
            <input
                type="email"
                placeholder="Email"
                name="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <button type={"submit"}>Submit</button>
        </form>
    )
}

export default EmployeeForm