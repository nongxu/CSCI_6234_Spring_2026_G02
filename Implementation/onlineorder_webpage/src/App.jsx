import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import CustomerLayout from "./layouts/CustomerLayout";
import OwnerLayout from "./layouts/OwnerLayout";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import RestaurantPage from "./pages/RestaurantPage";
import CartPage from "./pages/CartPage";
import OrderHistoryPage from "./pages/OrderHistoryPage";
import OwnerLoginPage from "./pages/OwnerLoginPage";
import OwnerSignupPage from "./pages/OwnerSignupPage";
import OwnerRegisterRestaurantPage from "./pages/OwnerRegisterRestaurantPage";
import OwnerDashboardPage from "./pages/OwnerDashboardPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<CustomerLayout />}>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/restaurants" element={<RestaurantPage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/orders" element={<OrderHistoryPage />} />
        </Route>
        <Route element={<OwnerLayout />}>
          <Route path="/owner/login" element={<OwnerLoginPage />} />
          <Route path="/owner/signup" element={<OwnerSignupPage />} />
          <Route
            path="/owner/register"
            element={<OwnerRegisterRestaurantPage />}
          />
          <Route path="/owner/dashboard" element={<OwnerDashboardPage />} />
        </Route>
        <Route path="/" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
