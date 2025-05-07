import { Route, Routes } from "react-router-dom"
import RegisterationPage from "./RegisterationPage"
import LoginPage from "./LoginPage"
import AdminDashboard from "./AdminDashboard"
import CustomerHomePage from "./CustomerHomePage"
import CartPage from "./CartPage"
import OrdersPage from "./OrderPage"
import AdminLogin from "./AdminLogin"
import Profile from "./Profile"

const AppRoutes = () => { 
    return (
        <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/register" element={<RegisterationPage />} />
            <Route path="/customerhome" element={<CustomerHomePage />} />
            <Route path="/adminhome" element={<AdminDashboard />} />
            <Route path="/UserCartPage" element={<CartPage/>} />
            <Route path="/orders" element={<OrdersPage />} />
            <Route path="/admin" element={<AdminLogin/>} />
            <Route path="/admindashboard" element={<AdminDashboard/>} />
        </Routes>
    )
}

export default AppRoutes