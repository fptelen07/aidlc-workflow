import { useEffect, useState, useCallback } from 'react';
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
import Chip from '@mui/material/Chip';
import Pagination from '@mui/material/Pagination';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import { orderService } from '../../services/orderService';

const STATUS_COLORS: Record<string, { color: string; bg: string }> = {
  pending: { color: '#1E40AF', bg: '#DBEAFE' },
  completed: { color: '#166534', bg: '#DCFCE7' },
  rejected: { color: '#991B1B', bg: '#FEE2E2' },
};

export default function MyOrders() {
  const { t } = useTranslation();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [orders, setOrders] = useState<any[]>([]);
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [status, setStatus] = useState('');
  const size = 10;

  const fetchOrders = useCallback(async () => {
    try {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const res: any = await orderService.getMyOrders({ page, size, status: status || undefined });
      const data = res?.data ?? res;
      setOrders(data?.records ?? data?.list ?? []);
      setTotal(data?.total ?? 0);
    } catch { setOrders([]); }
  }, [page, status]);

  useEffect(() => { fetchOrders(); }, [fetchOrders]);

  return (
    <Box sx={{ p: '24px 32px', display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Typography sx={{ fontSize: 24, fontWeight: 700 }}>{t('myOrders.title')}</Typography>

      <ToggleButtonGroup value={status} exclusive onChange={(_, v) => { setStatus(v ?? ''); setPage(1); }} size="small" data-testid="order-status-filter">
        <ToggleButton value="">{t('myOrders.all')}</ToggleButton>
        <ToggleButton value="pending">{t('myOrders.pending')}</ToggleButton>
        <ToggleButton value="completed">{t('myOrders.completed')}</ToggleButton>
        <ToggleButton value="rejected">{t('myOrders.rejected')}</ToggleButton>
      </ToggleButtonGroup>

      <Paper elevation={0} sx={{ borderRadius: 3, border: '1px solid #F1F5F9' }}>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: '#F8FAFC' }}>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('myOrders.product')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('myOrders.points')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('myOrders.status')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('myOrders.time')}</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {orders.map((order) => {
                const sc = STATUS_COLORS[order.status] ?? STATUS_COLORS.pending;
                return (
                  <TableRow key={order.id}>
                    <TableCell sx={{ fontSize: 13 }}>{order.productName}</TableCell>
                    <TableCell sx={{ fontSize: 13 }}>{order.pointsAmount?.toLocaleString()}</TableCell>
                    <TableCell><Chip label={t(`myOrders.${order.status}`)} size="small" sx={{ color: sc.color, bgcolor: sc.bg, fontSize: 11 }} /></TableCell>
                    <TableCell sx={{ fontSize: 13, color: 'text.secondary' }}>{order.createdAt}</TableCell>
                  </TableRow>
                );
              })}
              {orders.length === 0 && (
                <TableRow><TableCell colSpan={4} sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</TableCell></TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      {total > size && <Box sx={{ display: 'flex', justifyContent: 'center' }}>
        <Pagination count={Math.ceil(total / size)} page={page} onChange={(_, p) => setPage(p)} data-testid="order-pagination" />
      </Box>}
    </Box>
  );
}
