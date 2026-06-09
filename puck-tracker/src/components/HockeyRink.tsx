import { useRef, useEffect, useCallback, useState } from 'react';
import { Goal } from '../types';

interface HockeyRinkProps {
  goals: Goal[];
  selectedGoal: Goal | null;
  onGoalSelect: (goal: Goal) => void;
  onGoalDetail: (goal: Goal) => void;
  accentColor: string;
}

function rinkToPixel(rx: number, ry: number, rinkWidth: number, rinkHeight: number): { x: number; y: number } {
  const px = ((rx + 100) / 200) * rinkWidth;
  const py = ((ry + 42.5) / 85) * rinkHeight;
  return { x: px, y: py };
}

export default function HockeyRink({ goals, selectedGoal, onGoalSelect, onGoalDetail, accentColor }: HockeyRinkProps) {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const containerRef = useRef<HTMLDivElement>(null);
  const animFrameRef = useRef<number>(0);
  const startTimeRef = useRef<number>(0);
  const [dimensions, setDimensions] = useState({ width: 800, height: 340 });

  // Resize observer
  useEffect(() => {
    const container = containerRef.current;
    if (!container) return;

    const observer = new ResizeObserver((entries) => {
      for (const entry of entries) {
        const w = entry.contentRect.width;
        const h = w / 2.35;
        setDimensions({ width: w, height: h });
      }
    });

    observer.observe(container);
    // Initial size
    const w = container.clientWidth;
    setDimensions({ width: w, height: w / 2.35 });

    return () => observer.disconnect();
  }, []);

  const drawRink = useCallback((ctx: CanvasRenderingContext2D, w: number, h: number) => {
    // Background
    ctx.fillStyle = '#F8FAFC';
    ctx.beginPath();
    const r = 20;
    ctx.moveTo(r, 0);
    ctx.lineTo(w - r, 0);
    ctx.arcTo(w, 0, w, r, r);
    ctx.lineTo(w, h - r);
    ctx.arcTo(w, h, w - r, h, r);
    ctx.lineTo(r, h);
    ctx.arcTo(0, h, 0, h - r, r);
    ctx.lineTo(0, r);
    ctx.arcTo(0, 0, r, 0, r);
    ctx.closePath();
    ctx.fill();

    // Border
    ctx.strokeStyle = '#9CA3AF';
    ctx.lineWidth = 2;
    ctx.stroke();

    // Center red line
    ctx.strokeStyle = '#EF4444';
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(w / 2, 0);
    ctx.lineTo(w / 2, h);
    ctx.stroke();

    // Center circle
    ctx.beginPath();
    ctx.arc(w / 2, h / 2, h * 0.15, 0, Math.PI * 2);
    ctx.stroke();

    // Center dot
    ctx.fillStyle = '#EF4444';
    ctx.beginPath();
    ctx.arc(w / 2, h / 2, 3, 0, Math.PI * 2);
    ctx.fill();

    // Blue lines
    ctx.strokeStyle = '#3B82F6';
    ctx.lineWidth = 3;
    ctx.beginPath();
    ctx.moveTo(w * 0.35, 0);
    ctx.lineTo(w * 0.35, h);
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo(w * 0.65, 0);
    ctx.lineTo(w * 0.65, h);
    ctx.stroke();

    // Goal creases (left)
    ctx.strokeStyle = '#EF4444';
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.arc(w * 0.05, h / 2, h * 0.08, -1.5, 1.5);
    ctx.stroke();

    // Goal creases (right)
    ctx.beginPath();
    ctx.arc(w * 0.95, h / 2, h * 0.08, 1.5, 4.5);
    ctx.stroke();

    // Goal nets (left)
    ctx.strokeStyle = '#3B82F6';
    ctx.lineWidth = 1.5;
    ctx.beginPath();
    ctx.arc(w * 0.04, h / 2, h * 0.04, -1.5, 1.5);
    ctx.stroke();

    // Goal nets (right)
    ctx.beginPath();
    ctx.arc(w * 0.96, h / 2, h * 0.04, 1.5, 4.5);
    ctx.stroke();
  }, []);

  const drawDashedLine = useCallback((ctx: CanvasRenderingContext2D, x1: number, y1: number, x2: number, y2: number, color: string, lineWidth: number) => {
    ctx.strokeStyle = color;
    ctx.lineWidth = lineWidth;
    ctx.setLineDash([4, 4]);
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.stroke();
    ctx.setLineDash([]);
  }, []);

  // Animation loop
  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const { width: w, height: h } = dimensions;
    canvas.width = w * window.devicePixelRatio;
    canvas.height = h * window.devicePixelRatio;
    ctx.scale(window.devicePixelRatio, window.devicePixelRatio);

    startTimeRef.current = performance.now();

    const CYCLE_MS = 1800;

    const animate = (now: number) => {
      const elapsed = now - startTimeRef.current;
      const val = (elapsed % CYCLE_MS) / CYCLE_MS;
      // Ease in-out
      const eased = val < 0.5 ? 2 * val * val : 1 - Math.pow(-2 * val + 2, 2) / 2;

      ctx.clearRect(0, 0, w, h);
      drawRink(ctx, w, h);

      // Draw trajectory for selected goal
      if (selectedGoal) {
        const gol = selectedGoal;
        const startX = gol.x - 20.0 * (gol.x < 0 ? -1 : 1);
        const startY = gol.y > 0 ? gol.y - 12.0 : gol.y + 12.0;
        const netX = gol.x < 0 ? -89.0 : 89.0;
        const netY = 0.0;

        const startPx = rinkToPixel(startX, startY, w, h);
        const shotPx = rinkToPixel(gol.x, gol.y, w, h);
        const netPx = rinkToPixel(netX, netY, w, h);

        // Skater path (dashed blue)
        drawDashedLine(ctx, startPx.x, startPx.y, shotPx.x, shotPx.y, 'rgba(96,165,250,0.4)', 2);

        // Shot path (dashed amber)
        if (eased > 0.5) {
          drawDashedLine(ctx, shotPx.x, shotPx.y, netPx.x, netPx.y, 'rgba(251,191,36,0.6)', 2.5);
        }

        // Compute positions
        let skaterX: number, skaterY: number, puckX: number, puckY: number;
        if (eased <= 0.5) {
          const t = eased / 0.5;
          skaterX = startX + (gol.x - startX) * t;
          skaterY = startY + (gol.y - startY) * t;
          puckX = skaterX;
          puckY = skaterY;
        } else {
          const t = (eased - 0.5) / 0.5;
          skaterX = gol.x;
          skaterY = gol.y;
          puckX = gol.x + (netX - gol.x) * t;
          puckY = gol.y + (netY - gol.y) * t;
        }

        const skaterPx = rinkToPixel(skaterX, skaterY, w, h);
        const puckPx = rinkToPixel(puckX, puckY, w, h);

        // Draw skater
        ctx.beginPath();
        ctx.arc(skaterPx.x, skaterPx.y, 10, 0, Math.PI * 2);
        ctx.fillStyle = 'rgba(147,197,253,0.9)';
        ctx.fill();
        ctx.strokeStyle = '#fff';
        ctx.lineWidth = 1.5;
        ctx.stroke();
        ctx.fillStyle = '#fff';
        ctx.font = 'bold 9px Inter, sans-serif';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText('🏃', skaterPx.x, skaterPx.y);

        // Draw puck
        ctx.beginPath();
        ctx.arc(puckPx.x, puckPx.y, 8, 0, Math.PI * 2);
        ctx.fillStyle = '#111';
        ctx.fill();
        ctx.strokeStyle = '#FBBF24';
        ctx.lineWidth = 2;
        ctx.stroke();
        // Puck glow
        ctx.shadowColor = '#FBBF24';
        ctx.shadowBlur = 8;
        ctx.beginPath();
        ctx.arc(puckPx.x, puckPx.y, 4, 0, Math.PI * 2);
        ctx.fillStyle = '#FBBF24';
        ctx.fill();
        ctx.shadowBlur = 0;
      }

      // Draw all goal markers
      for (const gol of goals) {
        const px = rinkToPixel(gol.x, gol.y, w, h);
        const isSelected = selectedGoal?.eventId === gol.eventId;

        const pulseScale = isSelected
          ? 1.0 + 0.14 * Math.sin(eased * 2 * Math.PI)
          : 1.0 + 0.08 * Math.sin(eased * 2 * Math.PI);

        const radius = (isSelected ? 14 : 10) * pulseScale;

        // Glow
        ctx.shadowColor = isSelected ? 'rgba(251,191,36,0.8)' : 'rgba(239,68,68,0.6)';
        ctx.shadowBlur = isSelected ? 12 : 6;

        ctx.beginPath();
        ctx.arc(px.x, px.y, radius, 0, Math.PI * 2);
        ctx.fillStyle = isSelected ? '#FBBF24' : 'rgba(239,68,68,0.9)';
        ctx.fill();
        ctx.strokeStyle = '#fff';
        ctx.lineWidth = isSelected ? 3 : 2;
        ctx.stroke();
        ctx.shadowBlur = 0;

        // Period text
        ctx.fillStyle = isSelected ? '#000' : '#fff';
        ctx.font = `bold ${isSelected ? 11 : 9}px Inter, sans-serif`;
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(gol.period.toString(), px.x, px.y);
      }

      animFrameRef.current = requestAnimationFrame(animate);
    };

    animFrameRef.current = requestAnimationFrame(animate);

    return () => {
      if (animFrameRef.current) cancelAnimationFrame(animFrameRef.current);
    };
  }, [dimensions, goals, selectedGoal, drawRink, drawDashedLine]);

  // Handle click on canvas to detect goal dot clicks
  const handleCanvasClick = useCallback(
    (e: React.MouseEvent<HTMLCanvasElement>) => {
      const canvas = canvasRef.current;
      if (!canvas) return;

      const rect = canvas.getBoundingClientRect();
      const clickX = e.clientX - rect.left;
      const clickY = e.clientY - rect.top;
      const { width: w, height: h } = dimensions;

      for (const gol of goals) {
        const px = rinkToPixel(gol.x, gol.y, w, h);
        const dist = Math.sqrt((clickX - px.x) ** 2 + (clickY - px.y) ** 2);
        if (dist < 16) {
          onGoalSelect(gol);
          onGoalDetail(gol);
          return;
        }
      }
    },
    [goals, dimensions, onGoalSelect, onGoalDetail]
  );

  const handleReplay = useCallback(() => {
    startTimeRef.current = performance.now();
  }, []);

  return (
    <div className="hockey-rink-container" ref={containerRef}>
      <canvas
        ref={canvasRef}
        className="hockey-rink-canvas"
        style={{ width: dimensions.width, height: dimensions.height }}
        onClick={handleCanvasClick}
      />
      <button
        className="replay-btn"
        onClick={handleReplay}
        style={{ borderColor: `${accentColor}99` }}
      >
        <span className="replay-icon">▶</span>
        Replay Play
      </button>
    </div>
  );
}
