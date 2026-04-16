import { useEffect, useState, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
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
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import { orderService } from '../../services/orderService';

const STATUS_COLORS: Record<string, { color: string; bg: string }> = {
  pending: { color: '#1E40AF', bg: '#DBEAFE' },
  completed: { color: '#166534', bg: '#DCFCE7' },
  rejected: { color: '#991B1B', bg: '#FEE2E2' },
};

export default function AdminOrders() {
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
      const res: any = await orderService.listAllOrders({ page, size, status: status || undefined });
      const data = res?.data ?? res;
      setOrders(data?.records ?? data?.list ?? []);
      setTotal(data?.total ?? 0);
    } catch { setOrders([]); }
  }, [page, status]);

  useEffect(() => { fetchOrders(); }, [fetchOrders]);

  const handleConfirm = async (id: number) => {
    try { await orderService.confirmOrder(id); fetchOrders(); } catch { /* ignore */ }
  };

  const handleReject = async (id: number) => {
    try { await orderService.rejectOrder(id); fetchOrders(); } catch { /* ignore */ }
  };

  return (
    <Box sx={{ p: '32px', display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Typography sx={{ fontSize: 24, fontWeight: 700 }}>{t('adminOrders.title')}</Typography>

      <ToggleButtonGroup value={status} exclusive onChange={(_, v) => { setStatus(v ?? ''); setPage(1); }} size="small" data-testid="admin-order-status-filter">
        <ToggleButton value="">{t('adminOrders.all')}</ToggleButton>
        <ToggleButton value="pending">{t('adminOrders.pending')}</ToggleButton>
        <ToggleButton value="completed">{t('adminOrders.completed')}</ToggleButton>
        <ToggleButton value="rejected">{t('adminOrders.rejected')}</ToggleButton>
      </ToggleButtonGroup>

      <Paper elevation={0} sx={{ borderRadius: 3, border: '1px solid #F1F5F9' }}>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: '#F8FAFC' }}>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>ID</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminOrders.user')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminOrders.product')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminOrders.points')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminOrders.status')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminOrders.time')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminOrders.actions')}</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {orders.map((o) => {
                const sc = STATUS_COLORS[o.status] ?? STATUS_COLORS.pending;
                return (
                  <TableRow key={o.id}>
                    <TableCell sx={{ fontSize: 13 }}>{o.id}</TableCell>
                    <TableCell sx={{ fontSize: 13 }}>{o.userName || o.username}</TableCell>
                    <TableCell sx={{ fontSize: 13 }}>{o.productName}</TableCell>
                    <TableCell sx={{ fontSize: 13 }}>{o.pointsAmount?.toLocaleString()}</TableCell>
                    <TableCell><Chip label={t(`adminOrders.${o.status}`)} size="small" sx={{ color: sc.color, bgcolor: sc.bg, fontSize: 11 }} /></TableCell>
                    <TableCell sx={{ fontSize: 13, color: 'text.secondary' }}>{o.createdAt}</TableCell>
                    <TableCell>
                      {o.status === 'pending' && (
                        <>
                          <Button size="small" variant="contained" color="success" startIcon={<CheckIcon />} onClick={() => handleConfirm(o.id)} sx={{ mr: 1, textTransform: 'none', fontSize: 12 }} data-testid={`confirm-order-${o.id}`}>{t('adminOrders.confirm')}</Button>
                          <Button size="small" variant="outlined" color="error" startIcon={<CloseIcon />} onClick={() => handleReject(o.id)} sx={{ textTransform: 'none', fontSize: 12 }} data-testid={`reject-order-${o.id}`}>{t('adminOrders.reject')}</Button>
                        </>
                      )}
                    </TableCell>
                  </TableRow>
                );
              })}
              {orders.length === 0 && <TableRow><TableCell colSpan={7} sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</TableCell></TableRow>}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      {total > size && <Box sx={{ display: 'flex', justifyContent: 'center' }}><Pagination count={Math.ceil(total / size)} page={page} onChange={(_, p) => setPage(p)} data-testid="admin-order-pagination" /></Box>}
    </Box>
  );
}
