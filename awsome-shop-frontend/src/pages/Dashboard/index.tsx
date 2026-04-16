import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Link from '@mui/material/Link';
import Chip from '@mui/material/Chip';
import Inventory2Icon from '@mui/icons-material/Inventory2';
import GroupIcon from '@mui/icons-material/Group';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import TollIcon from '@mui/icons-material/Toll';
import { useNavigate } from 'react-router';
import { productService } from '../../services/productService';
import { pointsService } from '../../services/pointsService';
import { orderService } from '../../services/orderService';

const METRIC_CONFIG = [
  { key: 'totalProducts', icon: Inventory2Icon, iconColor: '#2563EB', iconBg: '#EFF6FF' },
  { key: 'totalUsers', icon: GroupIcon, iconColor: '#16A34A', iconBg: '#DCFCE7' },
  { key: 'monthlyRedemptions', icon: ShoppingCartIcon, iconColor: '#D97706', iconBg: '#FEF3C7' },
  { key: 'pointsCirculation', icon: TollIcon, iconColor: '#7C3AED', iconBg: '#EDE9FE' },
];

const STATUS_CONFIG: Record<string, { color: string; bg: string }> = {
  completed: { color: '#166534', bg: '#DCFCE7' },
  pending: { color: '#1E40AF', bg: '#DBEAFE' },
  processing: { color: '#92400E', bg: '#FEF3C7' },
  rejected: { color: '#991B1B', bg: '#FEE2E2' },
};

export default function Dashboard() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [metrics, setMetrics] = useState<any>({});
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [recentOrders, setRecentOrders] = useState<any[]>([]);

  useEffect(() => {
    // Fetch metrics from existing APIs
    productService.listProducts({ page: 1, size: 1 })
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .then((res: any) => { const d = res?.data ?? res; setMetrics((prev: any) => ({ ...prev, totalProducts: d?.total ?? 0 })); })
      .catch(() => {});
    pointsService.getStatistics()
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .then((res: any) => { const d = res?.data ?? res; setMetrics((prev: any) => ({ ...prev, pointsCirculation: d?.monthlyNet ?? 0, monthlyRedemptions: d?.monthlyDeducted ?? 0 })); })
      .catch(() => {});
    orderService.listAllOrders({ page: 1, size: 5 })
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .then((res: any) => { const d = res?.data ?? res; setRecentOrders(d?.records ?? d?.list ?? []); setMetrics((prev: any) => ({ ...prev, totalUsers: d?.total ?? 0 })); })
      .catch(() => {});
  }, []);

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: '24px', p: '32px' }}>
      <Typography sx={{ fontSize: 24, fontWeight: 700, color: 'text.primary' }}>{t('admin.dashboard')}</Typography>

      <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '20px' }}>
        {METRIC_CONFIG.map((mc) => {
          const IconComp = mc.icon;
          const value = metrics[mc.key] ?? 0;
          return (
            <Paper key={mc.key} elevation={0} sx={{ display: 'flex', flexDirection: 'column', gap: 1.5, p: 2.5, borderRadius: 3, border: '1px solid #F1F5F9' }}>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography sx={{ fontSize: 13, color: 'text.secondary' }}>{t(`admin.metrics.${mc.key}`)}</Typography>
                <Box sx={{ width: 36, height: 36, borderRadius: 2, bgcolor: mc.iconBg, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <IconComp sx={{ fontSize: 20, color: mc.iconColor }} />
                </Box>
              </Box>
              <Typography sx={{ fontSize: 28, fontWeight: 700, color: 'text.primary' }}>{Number(value).toLocaleString()}</Typography>
            </Paper>
          );
        })}
      </Box>

      <Paper elevation={0} sx={{ borderRadius: 3, border: '1px solid #F1F5F9', overflow: 'hidden' }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', px: 2.5, py: 2, borderBottom: '1px solid #F1F5F9' }}>
          <Typography sx={{ fontSize: 16, fontWeight: 600 }}>{t('admin.recentOrders')}</Typography>
          <Link component="button" underline="none" onClick={() => navigate('/admin/orders')} sx={{ fontSize: 13, color: 'primary.main' }}>{t('admin.viewAll')} →</Link>
        </Box>
        <TableContainer>
          <Table sx={{ '& .MuiTableCell-root': { borderColor: '#F1F5F9' } }}>
            <TableHead>
              <TableRow sx={{ bgcolor: '#F8FAFC' }}>
                <TableCell sx={{ fontSize: 12, fontWeight: 600, color: 'text.secondary', py: '10px', px: '20px' }}>{t('admin.table.user')}</TableCell>
                <TableCell sx={{ fontSize: 12, fontWeight: 600, color: 'text.secondary', py: '10px', px: '20px' }}>{t('admin.table.product')}</TableCell>
                <TableCell sx={{ fontSize: 12, fontWeight: 600, color: 'text.secondary', py: '10px', px: '20px' }}>{t('admin.table.points')}</TableCell>
                <TableCell sx={{ fontSize: 12, fontWeight: 600, color: 'text.secondary', py: '10px', px: '20px' }}>{t('admin.table.status')}</TableCell>
                <TableCell sx={{ fontSize: 12, fontWeight: 600, color: 'text.secondary', py: '10px', px: '20px' }}>{t('admin.table.time')}</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {recentOrders.map((order, idx) => {
                const statusCfg = STATUS_CONFIG[order.status] ?? STATUS_CONFIG.pending;
                return (
                  <TableRow key={order.id ?? idx} sx={{ '&:last-child td': { borderBottom: 0 } }}>
                    <TableCell sx={{ fontSize: 13, py: '12px', px: '20px' }}>{order.userName || order.user}</TableCell>
                    <TableCell sx={{ fontSize: 13, py: '12px', px: '20px' }}>{order.productName || order.product}</TableCell>
                    <TableCell sx={{ fontSize: 13, py: '12px', px: '20px' }}>{order.pointsAmount ?? order.points}</TableCell>
                    <TableCell sx={{ py: '12px', px: '20px' }}>
                      <Chip label={order.status} size="small" sx={{ fontSize: 11, fontWeight: 500, color: statusCfg.color, bgcolor: statusCfg.bg, borderRadius: '12px', height: 24 }} />
                    </TableCell>
                    <TableCell sx={{ fontSize: 13, color: 'text.secondary', py: '12px', px: '20px' }}>{order.createdAt || order.time}</TableCell>
                  </TableRow>
                );
              })}
              {recentOrders.length === 0 && <TableRow><TableCell colSpan={5} sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</TableCell></TableRow>}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Box>
  );
}
