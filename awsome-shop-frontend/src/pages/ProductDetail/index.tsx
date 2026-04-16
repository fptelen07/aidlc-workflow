import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Paper from '@mui/material/Paper';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import CircularProgress from '@mui/material/CircularProgress';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import TollIcon from '@mui/icons-material/Toll';
import Inventory2Icon from '@mui/icons-material/Inventory2';
import { productService } from '../../services/productService';
import { orderService } from '../../services/orderService';
import { pointsService } from '../../services/pointsService';

export default function ProductDetail() {
  const { id } = useParams<{ id: string }>();
  const { t } = useTranslation();
  const navigate = useNavigate();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [product, setProduct] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [userPoints, setUserPoints] = useState(0);

  useEffect(() => {
    pointsService.getBalance()
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .then((res: any) => { const d = res?.data ?? res; setUserPoints(d?.balance ?? 0); })
      .catch(() => {});
  }, []);

  useEffect(() => {
    if (!id) return;
    setLoading(true);
    productService.getProduct(Number(id))
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .then((res: any) => setProduct(res?.data ?? res))
      .catch(() => setProduct(null))
      .finally(() => setLoading(false));
  }, [id]);

  const canRedeem = product && userPoints >= product.pointsPrice && product.stock > 0;

  const handleRedeem = async () => {
    setSubmitting(true);
    try {
      await orderService.createOrder({ productId: product.id });
      setConfirmOpen(false);
      navigate('/orders');
    } catch { /* ignore */ }
    finally { setSubmitting(false); }
  };

  if (loading) return <Box sx={{ display: 'flex', justifyContent: 'center', py: 10 }}><CircularProgress /></Box>;
  if (!product) return <Box sx={{ textAlign: 'center', py: 10 }}><Typography>{t('productDetail.notFound')}</Typography></Box>;

  return (
    <Box sx={{ p: '32px', maxWidth: 900, mx: 'auto' }}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate(-1)} sx={{ mb: 3, textTransform: 'none' }} data-testid="back-btn">
        {t('productDetail.back')}
      </Button>
      <Paper elevation={0} sx={{ display: 'flex', gap: 4, p: 4, borderRadius: 3, border: '1px solid #F1F5F9' }}>
        <Box sx={{ width: 320, height: 320, bgcolor: '#DBEAFE', borderRadius: 2, display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
          {product.imageUrl ? <Box component="img" src={product.imageUrl} alt={product.name} sx={{ maxWidth: '100%', maxHeight: '100%', objectFit: 'contain' }} />
            : <Inventory2Icon sx={{ fontSize: 80, color: '#2563EB' }} />}
        </Box>
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, flex: 1 }}>
          <Typography sx={{ fontSize: 24, fontWeight: 700 }}>{product.name}</Typography>
          {product.categoryName && <Chip label={product.categoryName} size="small" sx={{ alignSelf: 'flex-start' }} />}
          <Typography sx={{ fontSize: 14, color: 'text.secondary', lineHeight: 1.8 }}>{product.description}</Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <TollIcon sx={{ color: '#D97706' }} />
            <Typography sx={{ fontSize: 28, fontWeight: 700, color: '#D97706' }}>{product.pointsPrice?.toLocaleString()}</Typography>
            <Typography sx={{ fontSize: 14, color: 'text.secondary', ml: 1 }}>{t('productDetail.points')}</Typography>
          </Box>
          <Typography sx={{ fontSize: 14, color: 'text.secondary' }}>{t('productDetail.stock')}: {product.stock}</Typography>
          <Button variant="contained" disabled={!canRedeem} onClick={() => setConfirmOpen(true)} data-testid="redeem-btn"
            sx={{ mt: 'auto', height: 48, borderRadius: 2, fontSize: 16, fontWeight: 600, textTransform: 'none', alignSelf: 'flex-start', px: 6 }}>
            {userPoints < (product.pointsPrice ?? 0) ? t('productDetail.insufficientPoints') : t('productDetail.redeem')}
          </Button>
        </Box>
      </Paper>

      <Dialog open={confirmOpen} onClose={() => setConfirmOpen(false)}>
        <DialogTitle>{t('productDetail.confirmTitle')}</DialogTitle>
        <DialogContent>
          <Typography>{t('productDetail.confirmMsg', { name: product.name, points: product.pointsPrice })}</Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmOpen(false)} data-testid="cancel-redeem">{t('common.cancel')}</Button>
          <Button variant="contained" onClick={handleRedeem} disabled={submitting} data-testid="confirm-redeem">{t('common.confirm')}</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
