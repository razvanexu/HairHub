import EmployeeForm from "../components/forms/EmployeeForm.tsx";
import ServiceTypeForm from "../components/forms/ServiceTypeForm.tsx";

function AdminPanel() {
    return (
        <>
            <EmployeeForm/>
            <ServiceTypeForm/>
        </>
    )
}

export default AdminPanel;