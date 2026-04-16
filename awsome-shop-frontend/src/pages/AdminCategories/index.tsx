import { useEffect, useState, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import IconButton from '@mui/material/IconButton';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { categoryService } from '../../services/categoryService';

export default function AdminCategories() {
  const { t } = useTranslation();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [categories, setCategories] = useState<any[]>([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [editItem, setEditItem] = useState<any>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);
  const [form, setForm] = useState({ name: '', sort: '' });

  const fetchCategories = useCallback(async () => {
    try {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const res: any = await categoryService.listCategories();
      setCategories(res?.data ?? res ?? []);
    } catch { setCategories([]); }
  }, []);

  useEffect(() => { fetchCategories(); }, [fetchCategories]);

  const openCreate = () => { setEditItem(null); setForm({ name: '', sort: '' }); setDialogOpen(true); };
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const openEdit = (item: any) => { setEditItem(item); setForm({ name: item.name, sort: String(item.sort ?? '') }); setDialogOpen(true); };

  const handleSave = async () => {
    try {
      if (editItem) {
        await categoryService.updateCategory({ id: editItem.id, name: form.name, sort: Number(form.sort) || 0 });
      } else {
        await categoryService.createCategory({ name: form.name, sort: Number(form.sort) || 0 });
      }
      setDialogOpen(false);
      fetchCategories();
    } catch { /* ignore */ }
  };

  const handleDelete = async () => {
    if (deleteId == null) return;
    try { await categoryService.deleteCategory(deleteId); setDeleteOpen(false); fetchCategories(); } catch { /* ignore */ }
  };

  return (
    <Box sx={{ p: '32px', display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography sx={{ fontSize: 24, fontWeight: 700 }}>{t('adminCategories.title')}</Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={openCreate} data-testid="add-category-btn" sx={{ textTransform: 'none' }}>{t('adminCategories.add')}</Button>
      </Box>

      <Paper elevation={0} sx={{ borderRadius: 3, border: '1px solid #F1F5F9' }}>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: '#F8FAFC' }}>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>ID</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminCategories.name')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminCategories.sort')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminCategories.actions')}</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {categories.map((c) => (
                <TableRow key={c.id}>
                  <TableCell sx={{ fontSize: 13 }}>{c.id}</TableCell>
                  <TableCell sx={{ fontSize: 13 }}>{c.name}</TableCell>
                  <TableCell sx={{ fontSize: 13 }}>{c.sort}</TableCell>
                  <TableCell>
                    <IconButton size="small" onClick={() => openEdit(c)} data-testid={`edit-cat-${c.id}`}><EditIcon sx={{ fontSize: 18 }} /></IconButton>
                    <IconButton size="small" onClick={() => { setDeleteId(c.id); setDeleteOpen(true); }} data-testid={`delete-cat-${c.id}`}><DeleteIcon sx={{ fontSize: 18, color: '#DC2626' }} /></IconButton>
                  </TableCell>
                </TableRow>
              ))}
              {categories.length === 0 && <TableRow><TableCell colSpan={4} sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</TableCell></TableRow>}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editItem ? t('adminCategories.edit') : t('adminCategories.add')}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '16px !important' }}>
          <TextField label={t('adminCategories.name')} value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} fullWidth size="small" data-testid="cat-form-name" />
          <TextField label={t('adminCategories.sort')} type="number" value={form.sort} onChange={(e) => setForm({ ...form, sort: e.target.value })} fullWidth size="small" data-testid="cat-form-sort" />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>{t('common.cancel')}</Button>
          <Button variant="contained" onClick={handleSave} data-testid="cat-form-save">{t('common.save')}</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={deleteOpen} onClose={() => setDeleteOpen(false)}>
        <DialogTitle>{t('adminCategories.deleteTitle')}</DialogTitle>
        <DialogContent><Typography>{t('adminCategories.deleteConfirm')}</Typography></DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteOpen(false)}>{t('common.cancel')}</Button>
          <Button variant="contained" color="error" onClick={handleDelete} data-testid="confirm-delete-cat">{t('common.delete')}</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
