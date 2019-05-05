package com.shareniu.bpmn.ch14;

import org.flowable.bpmn.model.AssociationDirection;
import org.flowable.image.impl.DefaultProcessDiagramCanvas;
import org.flowable.image.util.ReflectUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ShareniuDefaultProcessDiagramCanvas extends DefaultProcessDiagramCanvas {

    protected static Color HIGHLIGHT_COLOR = Color.blue;
    protected static Color HIGHLIGHT_FLOW_COLOR = Color.MAGENTA;

    public ShareniuDefaultProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
        super(width, height, minX, minY, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);

        DefaultProcessDiagramCanvas.HIGHLIGHT_COLOR=Color.blue;
    }

    public void drawConnection(int[] xPoints, int[] yPoints, boolean conditional, boolean isDefault, String connectionType,
                               AssociationDirection associationDirection, boolean highLighted, double scaleFactor) {

        Paint originalPaint = g.getPaint();
        Stroke originalStroke = g.getStroke();

        g.setPaint(CONNECTION_COLOR);
        if (connectionType.equals("association")) {
            g.setStroke(ASSOCIATION_STROKE);
        } else if (highLighted) {
            g.setPaint(HIGHLIGHT_FLOW_COLOR);
            g.setStroke(HIGHLIGHT_FLOW_STROKE);
        }

        for (int i = 1; i < xPoints.length; i++) {
            Integer sourceX = xPoints[i - 1];
            Integer sourceY = yPoints[i - 1];
            Integer targetX = xPoints[i];
            Integer targetY = yPoints[i];
            Line2D.Double line = new Line2D.Double(sourceX, sourceY, targetX, targetY);
            g.draw(line);
        }

        if (isDefault) {
            Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            drawDefaultSequenceFlowIndicator(line, scaleFactor);
        }

        if (conditional) {
            Line2D.Double line = new Line2D.Double(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            drawConditionalSequenceFlowIndicator(line, scaleFactor);
        }

        if (associationDirection == AssociationDirection.ONE || associationDirection == AssociationDirection.BOTH) {
            Line2D.Double line = new Line2D.Double(xPoints[xPoints.length - 2], yPoints[xPoints.length - 2], xPoints[xPoints.length - 1], yPoints[xPoints.length - 1]);
            drawArrowHead(line, scaleFactor);
        }
        if (associationDirection == AssociationDirection.BOTH) {
            Line2D.Double line = new Line2D.Double(xPoints[1], yPoints[1], xPoints[0], yPoints[0]);
            drawArrowHead(line, scaleFactor);
        }
        g.setPaint(originalPaint);
        g.setStroke(originalStroke);
    }
    public void drawSequenceflow(int srcX, int srcY, int targetX, int targetY, boolean conditional, boolean highLighted, double scaleFactor) {
        Paint originalPaint = g.getPaint();
        if (highLighted)
            g.setPaint(HIGHLIGHT_FLOW_COLOR);

        Line2D.Double line = new Line2D.Double(srcX, srcY, targetX, targetY);
        g.draw(line);
        drawArrowHead(line, scaleFactor);

        if (conditional) {
            drawConditionalSequenceFlowIndicator(line, scaleFactor);
        }

        if (highLighted)
            g.setPaint(originalPaint);
    }
    public void initialize(String imageType) {
        if ("png".equalsIgnoreCase(imageType)) {
            this.processDiagram = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        } else {
            this.processDiagram = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        }

        this.g = processDiagram.createGraphics();
        if (!"png".equalsIgnoreCase(imageType)) {
            this.g.setBackground(new Color(255, 255, 255, 0));
            this.g.clearRect(0, 0, canvasWidth, canvasHeight);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(Color.black);

        Font font = new Font(activityFontName, Font.BOLD, 16);
        g.setFont(font);
        this.fontMetrics = g.getFontMetrics();

        LABEL_FONT = new Font(labelFontName, Font.ITALIC, 16);
        ANNOTATION_FONT = new Font(annotationFontName, Font.PLAIN, FONT_SIZE);

        try {
            USERTASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/receiveTask.png", customClassLoader));
            SCRIPTTASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/scriptTask.png", customClassLoader));
            SERVICETASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/serviceTask.png", customClassLoader));
            RECEIVETASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/receiveTask.png", customClassLoader));
            SENDTASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/sendTask.png", customClassLoader));
            MANUALTASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/manualTask.png", customClassLoader));
            BUSINESS_RULE_TASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/businessRuleTask.png", customClassLoader));
            SHELL_TASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/shellTask.png", customClassLoader));
            DMN_TASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/dmnTask.png", customClassLoader));
            CAMEL_TASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/camelTask.png", customClassLoader));
            MULE_TASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/muleTask.png", customClassLoader));
            HTTP_TASK_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/httpTask.png", customClassLoader));

            TIMER_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/timer.png", customClassLoader));
            COMPENSATE_THROW_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/compensate-throw.png", customClassLoader));
            COMPENSATE_CATCH_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/compensate.png", customClassLoader));
            ERROR_THROW_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/error-throw.png", customClassLoader));
            ERROR_CATCH_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/error.png", customClassLoader));
            MESSAGE_THROW_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/message-throw.png", customClassLoader));
            MESSAGE_CATCH_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/message.png", customClassLoader));
            SIGNAL_THROW_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/signal-throw.png", customClassLoader));
            SIGNAL_CATCH_IMAGE = ImageIO.read(ReflectUtil.getResource("org/flowable/icons/signal.png", customClassLoader));
        } catch (IOException e) {
            LOGGER.warn("Could not load image for process diagram creation: {}", e.getMessage());
        }
    }
}
