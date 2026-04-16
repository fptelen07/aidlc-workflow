import { createBrowserRouter } from 'react-router';
import Login from '../pages/Login';
import Register from '../pages/Register';
import NotFound from '../pages/NotFound';
import EmployeeLayout from '../components/Layout/EmployeeLayout';
import AdminLayout from '../components/Layout/AdminLayout';
import ShopHome from '../pages/ShopHome';
import ProductDetail from '../pages/ProductDetail';
import MyOrders from '../pages/MyOrders';
import MyPoints from '../pages/MyPoints';
import Profile from '../pages/Profile';
import Dashboard from '../pages/Dashboard';
import AdminProducts from '../pages/AdminProducts';
import AdminCategories from '../pages/AdminCategories';
import AdminOrders from '../pages/AdminOrders';
import AdminPoints from '../pages/AdminPoints';
import AdminUsers from '../pages/AdminUsers';
import AuthGuard from './AuthGuard';

const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login />,
  },
  {
    path: '/register',
    element: <Register />,
  },
  // Employee routes
  {
    path: '/',
    element: (
      <AuthGuard requiredRole="employee">
        <EmployeeLayout />
      </AuthGuard>
    ),
    children: [
      { index: true, element: <ShopHome /> },
      { path: 'product/:id', element: <ProductDetail /> },
      { path: 'orders', element: <MyOrders /> },
      { path: 'points', element: <MyPoints /> },
      { path: 'profile', element: <Profile /> },
    ],
  },
  // Admin routes
  {
    path: '/admin',
    element: (
      <AuthGuard requiredRole="admin">
        <AdminLayout />
      </AuthGuard>
    ),
    children: [
      { index: true, element: <Dashboard /> },
      { path: 'products', element: <AdminProducts /> },
      { path: 'categories', element: <AdminCategories /> },
      { path: 'orders', element: <AdminOrders /> },
      { path: 'points', element: <AdminPoints /> },
      { path: 'users', element: <AdminUsers /> },
    ],
  },
  {
    path: '*',
    element: <NotFound />,
  },
]);

export default router;
